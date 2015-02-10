// Multiple video elements loaded at the same time

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

var playGlyphClass = "glyphicon-play";
var pauseGlyphClass = "glyphicon-pause";

var videoPlayer;
var videos;
var index = 0;
var totalDuration = 0;
var passedTime = 0;

function playPauseButton() {
    var button = document.getElementById(playPauseId);

    if (videoPlayer.paused) {
        button.class = pauseClass;
        button.getElementsByClassName(pauseGlyphClass)[0].style.display = "inline";
        button.getElementsByClassName(playGlyphClass)[0].style.display = "none";

        videoPlayer.play();
    } else {
        button.class = playClass;
        button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
        button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";

        videoPlayer.pause();
    }
};

function stopButton() {
    index = 0;
    switchToVideo();
    resetButtons();
};

function resetButtons() {
    var button = document.getElementById(playPauseId);
    button.getElementsByClassName(playGlyphClass)[0].style.display = "inline";
    button.getElementsByClassName(pauseGlyphClass)[0].style.display = "none";
}

function volume(direction) {
    var value = videoPlayer.volume;
    if (direction == "up") value += 0.1;
    else
    if (direction == "down") value -= 0.1;

    if (0 > value) value = 0;
    else
    if (value > 1) value = 1;

    for (var i = 0; i < videos.length; ++ i)
        videos[i].volume = value;
};

function updateProgressBar() {
    var progressBar = document.getElementById(progressBarId);
    var value = Math.floor(100 * (videoPlayer.currentTime + passedTime) / totalDuration);
    progressBar.value = value;
};

function switchToVideo(arg) {
    var sign = 1;

    if (arg == '-') {
        index -= 2;
        sign = -1;
        if (index < 0) index = 0;
    }

    if (videoPlayer) {
        videoPlayer.style.display = "none";
        videoPlayer.pause();
        videoPlayer.currentTime = 0;
        if (index != 0) passedTime += videoPlayer.duration * sign;
        else {
            passedTime = 0;
            resetButtons();
        }
        videoPlayer.pause();
    }

    videoPlayer = videos[index];
    videoPlayer.style.display = "inline";
    if (index > 0) videoPlayer.play();

    index = (index + 1) % videos.length;
};

function addDuration(e) {
    totalDuration += videos[e.target.id].duration;
    console.log(e.target.id);
};

function getVideos() {
    videos = document.getElementsByTagName("video");

    for (var i = 0; i < videos.length; ++ i) {
        videos[i].id = i;
        videos[i].addEventListener("loadedmetadata", addDuration, false);
        videos[i].addEventListener("timeupdate", updateProgressBar, false);
        videos[i].addEventListener("ended", switchToVideo, false);
    }

    videoPlayer = undefined;

    switchToVideo();
};

document.addEventListener("DOMContentLoaded", function() { getVideos(); }, false);
