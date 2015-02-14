var recorderContainerId = "recorder-container";
var recorderAudioId = "recorder-audio";
var recorderVideoId = "recorder-video";
var overlayFullId = "overlay-full";
var overlayTopId = "overlay-top";
var retryId = "retry-button";
var startStopPlayPauseId = "start-stop-play-pause-button";
var nextId = "next-button";
var publishId = "publish";

var questionsAttr = "data-questions";
var durationsAttr = "data-durations";

var startGlyph = "glyphicon glyphicon-record";
var stopGlyph = "glyphicon glyphicon-stop";
var playGlyph = "glyphicon glyphicon-play";
var pauseGlyph = "glyphicon glyphicon-pause";

var recorderAudio;
var recorderVideo;
var overlayFull;
var overlayTop;
var btnRetry;
var btnStartStopPlayPause;
var btnNext;

var questions;
var durations;
var audioUrls;
var videoUrls;
var audioBlobs;
var videoBlobs;
var btnStartStopPlayPauseState;
var index;

var cameraStream;

var mRecordRTC = new MRecordRTC();

mRecordRTC.media = {
    audio: true,
    video: true
};

mRecordRTC.video = mRecordRTC.canvas = {
    width: 640,
    height: 480
};

//navigator.getUserMedia =
//    navigator.getUserMedia ||
//    navigator.webkitGetUserMedia ||
//    navigator.mozGetUserMedia ||
//    navigator.msGetUserMedia;

// TODO: check if it is better to go directly into startRecording
function retry() {
    recorderAudio.src = "";
    recorderVideo.src = "";

    stateBEGIN();
};

// TODO: must never use the HTML5 default video controls
function startStopPlayPause() {
    switch (btnStartStopPlayPauseState) {
        // TODO: check onAudioProcessStarted to solve delay between audio and video
        case "start":
            recorderVideo.src = window.URL.createObjectURL(cameraStream);
            mRecordRTC.addStream(cameraStream);

            recorderAudio.play();
            recorderVideo.play();

            setTimeout(function() {mRecordRTC.startRecording();}, 1000);

            stateREC();
            break;
        case "stop":
            var notAudio = true;
            var notVideo = true;
            mRecordRTC.stopRecording(function(url, type) {
                if (type == "audio") {
                    recorderAudio.pause();
                    recorderAudio.src = url;

                    audioUrls[index] = url;

                    notAudio = false;
                }

                if (type == "video") {
                    recorderVideo.pause();
                    recorderVideo.src = url;

                    videoUrls[index] = url;

                    notVideo = false;
                }

                mRecordRTC.writeToDisk();

                if (!notAudio && !notVideo) statePLAYBACK();
            });

            // TODO: look for other solution, otherwise leave it like this (audio not working on firefox)
            if (notAudio || notVideo) {
                var notReload = true;

                MRecordRTC.getFromDisk("all", function (dataURL, type) {
                    if (type == "audio" && notAudio) {
                        recorderAudio.pause();
                        recorderAudio.src = dataURL;

                        audioUrls[index] = dataURL;

                        notAudio = false;
                    }

                    if (type == "video" && notVideo) {
                        recorderVideo.pause();
                        recorderVideo.src = dataURL;

                        videoUrls[index] = dataURL;

                        notVideo = false;
                    }

                    if (!notAudio && !notVideo && notReload) {
                        statePLAYBACK();
                        notReload = false;
                    }
                });
            }
            break;
        case "play":
            recorderAudio.play();
            recorderVideo.play();

            change(btnStartStopPlayPause, "inline", pauseGlyph);
            btnStartStopPlayPauseState = "pause";
            break;
        // TODO: when video playback finishes the button must go back to play
        case "pause":
            recorderAudio.pause();
            recorderVideo.pause();

            change(btnStartStopPlayPause, "inline", playGlyph);
            btnStartStopPlayPauseState = "play";
            break;
    }
};

// TODO: warning! watch out for asynchronous call of writeToDisk; I do not use getFromDisk, but be ware of it!
function next() {
    recorderAudio.src = "";
    recorderVideo.src = "";

    mRecordRTC.getBlob(function(blobs) {
        audioBlobs[index] = blobs.audio;
        videoBlobs[index] = blobs.video;

        // TODO: take care when index out of bounds
        index ++;
        if (index >= questions.length) statePUBLISH();
        else stateBEGIN();
    });
};

function change(button, display, className) {
    button.style.display = display;

    if (className == undefined) return;
    button.getElementsByTagName("span")[0].className = className;
};

// TODO: some changes can be skipped because of the state transition restriction
function stateBEGIN() {
    overlayTop.style.display = "none";

    overlayFull.innerHTML = questions[index];
    overlayFull.style.display = "inline";

    change(btnRetry, "none");
    change(btnStartStopPlayPause, "inline", startGlyph);
    btnStartStopPlayPauseState = "start";
    change(btnNext, "inline");
};

function stateREC() {
    overlayTop.innerHTML = questions[index];
    overlayTop.style.display = "inline";

    overlayFull.style.display = "none";

    change(btnRetry, "none");
    change(btnStartStopPlayPause, "inline", stopGlyph);
    btnStartStopPlayPauseState = "stop";
    change(btnNext, "none");
};

function statePLAYBACK() {
    overlayTop.innerHTML = questions[index];
    overlayTop.style.display = "inline";

    overlayFull.style.display = "none";

    change(btnRetry, "inline");
    change(btnStartStopPlayPause, "inline", playGlyph);
    btnStartStopPlayPauseState = "play";
    change(btnNext, "inline");
};

function statePUBLISH() {
    // TODO: camera is left on even if the user is not recording; this is bad but avoid access to camera question
    cameraStream.stop();

    document.getElementById(publishId).style.display = "inline";

    // TODO: hide the recorder or switch pages
    var child = document.getElementById(recorderContainerId);
    child.parentNode.removeChild(child);
};

// TODO: mirrored image of the camera only when recording; it goes back then; bad call;
// TODO: I am losing frames with this setup; so if you click play and immediately say something, that info might get lost
function initRecorder() {
    recorderAudio = document.getElementById(recorderAudioId);

    recorderVideo = document.getElementById(recorderVideoId);
    recorderVideo.volume = 0;

    overlayFull = document.getElementById(overlayFullId);
    overlayTop = document.getElementById(overlayTopId);

    btnRetry = document.getElementById(retryId);
    btnStartStopPlayPause = document.getElementById(startStopPlayPauseId);
    btnNext = document.getElementById(nextId);

    questions = document.getElementById(recorderContainerId).getAttribute(questionsAttr);
    questions = JSON.parse(questions);

    durations = document.getElementById(recorderContainerId).getAttribute(durationsAttr);
    durations = JSON.parse(durations);

    audioUrls = new Array(questions.length);
    videoUrls = new Array(questions.length);
    audioBlobs = new Array(questions.length);
    videoBlobs = new Array(questions.length);

    index = 0;

    navigator.getUserMedia({
        audio: true,
        video: true
    }, function (stream) {
        cameraStream = stream;

        stateBEGIN();
    }, function (e) {
        console.log(e);
    });
};

document.addEventListener("DOMContentLoaded", function() { initRecorder(); }, false);