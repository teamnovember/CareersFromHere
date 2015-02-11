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

var rafId;
var frames = [];

function startRecording() {
    var canvas = document.createElement("canvas");
    var ctx = canvas.getContext("2d");

    var width = canvas.width;
    var height = canvas.height;

    function drawVideoFrame(time) {
        rafId = requestAnimationFrame(drawVideoFrame);
        ctx.drawImage(recorder, 0, 0, width, height);
        frames.push(canvas.toDataURL("image/webp", 1));
    };

    rafId = requestAnimationFrame(drawVideoFrame);
};

function stopRecording() {
    cancelAnimationFrame(rafId);

    var webmBlob = Whammy.fromImageArray(frames, 1000 / 30);

    var player = document.getElementById("video-player");
    player.src = window.URL.createObjectURL(webmBlob);
};

document.addEventListener("DOMContentLoaded", function() { initialiseRecorder(); }, false);