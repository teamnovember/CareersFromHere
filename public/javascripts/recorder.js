var recorderContainerId = "recorder-container";
var recorderAudioId = "recorder-audio";
var recorderVideoId = "recorder-video";
var overlayFullId = "overlay-full";
var overlayTopId = "overlay-top";
var retryId = "retry-button";
var startStopPlayPauseId = "start-stop-play-pause-button";
var nextId = "next-button";
var publishId = "publish";
var titleId = "video-title";
var descriptionId = "video-description";
var leftClockId = "left-clock";
var rightClockId = "right-clock";

var progressClass = "progress";
var progressBarClass = "progress-bar";

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
var newrec;
var durationAudio;
var durationVideo;
var oldTime;
var firstTimeUpdate;
var leftClock;
var rightClock;
var progress;
var state;

var cameraStream;

var mRecordRTC = new MRecordRTC();

mRecordRTC.media = {
    audio: true,
    video: true
};

// TODO: trying 640x360, does not resize properly
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

            // setTimeout(function() {mRecordRTC.startRecording();}, 1000);
            firstTimeUpdate = true;
            mRecordRTC.startRecording();
            recorderVideo.addEventListener("timeupdate", updateProgressREC, false);

            stateREC();
            break;
        case "stop":
            newrec = true;

            var notAudio = true;
            var notVideo = true;

            recorderVideo.removeEventListener("timeupdate", updateProgressREC, false);

            mRecordRTC.stopRecording(function(url, type) {
                if (type == "audio") {
                    recorderAudio.pause();
                    recorderAudio.src = url;

                    audioUrls[index] = url;

                    notAudio = false;
                }

                if (type == "video") {
                    //oldTime = recorderVideo.currentTime;
                    recorderVideo.pause();
                    recorderVideo.src = url;

                    videoUrls[index] = url;

                    notVideo = false;
                }

                mRecordRTC.writeToDisk();

                if (!notAudio && !notVideo) statePLAYBACK();
            });

            // TODO: look for other solution, otherwise leave it like this (audio not working on firefox)
            // TODO: seems like getting audio from disk works (on dummy, but not here)
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
        if (newrec) {
            audioBlobs[index] = blobs.audio;
            videoBlobs[index] = blobs.video;

            newrec = false;
        }

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
    state = "BEGIN";
    recorderVideo.removeEventListener("timeupdate", updateProgressPLAYBACK, false);

    overlayTop.style.display = "none";

    overlayFull.innerHTML = questions[index];
    overlayFull.style.display = "inline";

    change(btnRetry, "none");
    change(btnStartStopPlayPause, "inline", startGlyph);
    btnStartStopPlayPauseState = "start";
    change(btnNext, "inline");

    leftClock.innerText = "";
    rightClock.innerText = "";
    document.getElementsByClassName(progressBarClass)[0].style.width = "0%";
};

function stateREC() {
    state = "REC";
    recorderVideo.removeEventListener("timeupdate", updateProgressPLAYBACK, false);

    overlayTop.innerHTML = questions[index];
    overlayTop.style.display = "inline";

    overlayFull.style.display = "none";

    change(btnRetry, "none");
    change(btnStartStopPlayPause, "inline", stopGlyph);
    btnStartStopPlayPauseState = "stop";
    change(btnNext, "none");

    leftClock.innerText = 0;
    rightClock.innerText = durations[index];
    document.getElementsByClassName(progressBarClass)[0].style.width = "0%";
};

function statePLAYBACK() {
    state = "PLAYBACK";
    recorderVideo.addEventListener("timeupdate", updateProgressPLAYBACK, false);

    overlayTop.innerHTML = questions[index];
    overlayTop.style.display = "inline";

    overlayFull.style.display = "none";

    change(btnRetry, "inline");
    change(btnStartStopPlayPause, "inline", playGlyph);
    btnStartStopPlayPauseState = "play";
    change(btnNext, "inline");

    leftClock.innerText = 0;
    // right clock is done on updDurationVideo()
    document.getElementsByClassName(progressBarClass)[0].style.width = "0%";
};

function statePUBLISH() {
    // TODO: camera is left on even if the user is not recording; this is bad but avoid access to camera question
    cameraStream.stop();

    document.getElementById(publishId).style.display = "inline";

    // TODO: hide the recorder or switch pages
    var child = document.getElementById(recorderContainerId);
    child.parentNode.removeChild(child);
};

function publish() {
    // TODO: this should be user specific
    var name = "qwe";

    var videoType = "video";
    var videoExt = ".webm";

    var audioType = "audio";
    var audioExt = ".wav";

    var formData = new FormData();

    formData.append(videoType + "-title", document.getElementById(titleId).value);
    formData.append(videoType + "-description", document.getElementById(descriptionId).value);

    for (var i = 0; i < index; ++ i)
        if (videoBlobs[i] != undefined && audioBlobs[i] != undefined) {
            formData.append(videoType + "-questionId", i);
            formData.append(videoType + "-filename", name + i + videoExt);
            formData.append(videoType + "-duration", durationVideo[i]);
            formData.append(videoType + "-blob", videoBlobs[i]);

            formData.append(audioType + "-filename", name + i + audioExt);
            formData.append(audioType + "-duration", durationAudio[i]);
            formData.append(audioType + "-blob", audioBlobs[i]);
        }

    console.log(formData);

    xhr("/record/publish", formData, function (fName) {
        window.open(location.href + fName);
    });

    // TODO: callback useless
    function xhr(url, data, callback) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                window.location.href = "/";
            }
        };
        request.open("POST", url);
        request.send(data);
    }
};

function updDurationAudio() {
    durationAudio[index] = recorderAudio.duration;
};

function updDurationVideo() {
    durationVideo[index] = recorderVideo.duration;

    if (state == "PLAYBACK") rightClock.innerText = durationVideo[index];
};

function testDuration() {
    if (firstTimeUpdate) {
        oldTime = recorderVideo.currentTime;
        firstTimeUpdate = false;
    }
    if (recorderVideo.currentTime - oldTime >= durations[index]) btnStartStopPlayPause.click();
};

function updateProgressREC() {
    if (firstTimeUpdate) return;

    leftClock.innerText = recorderVideo.currentTime - oldTime;
    rightClock.innerText = durations[index] - recorderVideo.currentTime + oldTime;

    var percentage = (recorderVideo.currentTime - oldTime) / durations[index] * 100;
    percentage = percentage.toString() + "%";

    document.getElementsByClassName(progressBarClass)[0].style.width = percentage;
};

function updateProgressPLAYBACK() {
    leftClock.innerText = recorderVideo.currentTime;
    rightClock.innerText = durationVideo[index] - recorderVideo.currentTime;

    var percentage = recorderVideo.currentTime / durationVideo[index] * 100;
    percentage = percentage.toString() + "%";

    document.getElementsByClassName(progressBarClass)[0].style.width = percentage;
};

// TODO: mirrored image of the camera only when recording; it goes back then; bad call;
// TODO: I am losing frames with this setup; so if you click play and immediately say something, that info might get lost
function initRecorder() {
    recorderAudio = document.getElementById(recorderAudioId);

    recorderVideo = document.getElementById(recorderVideoId);
    recorderVideo.volume = 0;

    recorderAudio.addEventListener("loadedmetadata", updDurationAudio, false);
    recorderVideo.addEventListener("loadedmetadata", updDurationVideo, false);
    recorderVideo.addEventListener("timeupdate", testDuration, false);

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
    newrec = false;
    oldTime = 0;

    durationAudio = new Array(questions.length);
    durationVideo = new Array(questions.length);

    leftClock = document.getElementById(leftClockId);
    rightClock = document.getElementById(rightClockId);
    progress = document.getElementsByClassName(progressClass)[0];

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