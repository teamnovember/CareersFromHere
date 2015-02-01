var recorderId = "recorder";

var recorder;

function initialiseRecorder() {
    recorder = document.getElementById(recorderId);

    navigator.getUserMedia = navigator.getUserMedia ||
                                navigator.webkitGetUserMedia ||
                                navigator.mozGetUserMedia ||
                                navigator.msGetUserMedia;

    navigator.getUserMedia({video: true}, function(stream) {
        recorder.src = window.URL.createObjectURL(stream);
    }, function(e) {
        console.log(e);
    });
};

document.addEventListener("DOMContentLoaded", function() { initialiseRecorder(); }, false);