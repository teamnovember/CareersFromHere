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
    }, function (stream) {
        recorderVideo.src = window.URL.createObjectURL(stream);

        mRecordRTC.addStream(stream);
    }, function (e) {
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

function go(blob) {
    var fileType = 'video';
    var fileName = 'ABCDEF.webm';

    var formData = new FormData();
    formData.append(fileType + '-filename', fileName);
    formData.append(fileType + '-blob', blob);

    xhr('/dummy', formData, function (fName) {
        window.open(location.href + fName);
    });

    function xhr(url, data, callback) {
        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                callback(location.href + request.responseText);
            }
        };
        request.open('POST', url);
        request.send(data);
    }
};

function up() {
    MRecordRTC.getFromDisk("all", function(dataURL, type) {
        if (type == "video") {
            console.log(dataURL);
        }
    });

    mRecordRTC.getBlob(function(blobs) {
        console.log(blobs.video);
        go(blobs.video);
    });
};

document.addEventListener("DOMContentLoaded", function() { initialiseRecorder(); }, false);