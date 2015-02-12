var recorderVideo;
var recorderAudio;

var mRecordRTC = new MRecordRTC();

mRecordRTC.media = {
    audio: true,
    video: true
};

function initialiseRecorder() {
    recorderAudio = document.getElementById("recorder-audio");

    recorderVideo = document.getElementById("recorder-video");
    recorderVideo.volume = 0;

    navigator.getUserMedia = navigator.getUserMedia ||
                                navigator.webkitGetUserMedia ||
                                navigator.mozGetUserMedia ||
                                navigator.msGetUserMedia;

    navigator.getUserMedia({
        audio: true,
        video: true
    }, function(stream) {
        recorderVideo.src = window.URL.createObjectURL(stream);

        mRecordRTC.addStream(stream);
    }, function(e) {
        console.log(e);
    });
};

function startRecording() {
    mRecordRTC.startRecording();
};

function stopRecording() {
    mRecordRTC.stopRecording(function(url, type) {
        if (type == "audio") {
            recorderAudio.src = url;
            recorderAudio.play();
        }
        if (type == "video") {
            recorderVideo.src = url;
            recorderVideo.play();
        }

        mRecordRTC.writeToDisk();
    });
};

document.addEventListener("DOMContentLoaded", function() { initialiseRecorder(); }, false);