var videoPlayerId = "video-player";
var playPauseId = "play-pause-button";
var stopId = "stop-button";
var volumeUpId = "volume-up-button";
var volumeDownId = "volume-down-button";
var progressBarId = "progress-bar";


var playClass = "play";
var pauseClass = "pause";
var stopClass = "stop";
var volumeUpClass = "volume-up";
var volumeDownClass = "volume-down";

var playInnerHTML = "play";
var pauseInnerHTML = "pause";
var stopInnerHTML = "stop";
var volumeUpInnerHTML = "volume up";
var volumeDownInnerHTML = "volume down";

var videoPlayer;

function initialise() {
    videoPlayer = document.getElementById(videoPlayerId);
    videoPlayer.controls = false;

    videoPlayer.addEventListener("timeupdate", updateProgressBar, false);
};

function changeButton(button, btnClass, btnInnerHTML) {
    button.class = btnClass;
    button.innerHTML = btnInnerHTML;
};

function playPauseButton(type) {
    var button = document.getElementById(playPauseId);

    // TODO: handle case when video stops (switch button to play)

    if (type == playClass) changeButton(button, playClass, playInnerHTML);
    else
    if (type == pauseClass) changeButton(button, pauseClass, pauseInnerHTML);
    else
    if (videoPlayer.paused || videoPlayer.ended) {
        changeButton(button, pauseClass, pauseInnerHTML);
        videoPlayer.play();
    } else {
        changeButton(button, playClass, playInnerHTML);
        videoPlayer.pause();
    }
}

function stopButton() {
    videoPlayer.pause();
    videoPlayer.currentTime = 0;
}

function volume(direction) {
    var value = videoPlayer.volume;
    if (direction == "up") value += 0.1;
    else
    if (direction == "down") value -= 0.1;

    if (0 > value) value = 0;
    else
    if (value > 1) value = 1;

    videoPlayer.volume = value;
}

function updateProgressBar() {
    var progressBar = document.getElementById(progressBarId);
    var value = Math.floor(100 / videoPlayer.duration) * videoPlayer.currentTime;
    progressBar.value = value;

    progressBar.innerHTML = value + "% played";
}

document.addEventListener("DOMContentLoaded", function() { initialise(); }, false);