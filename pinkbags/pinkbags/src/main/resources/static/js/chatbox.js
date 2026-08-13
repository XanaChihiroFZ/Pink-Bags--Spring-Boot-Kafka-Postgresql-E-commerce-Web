$(document).ready(function () {
  const db = firebase.database();

  // Toggle chatbox
  $("#openChat").click(function () {
    $("#chatBoxContainer").toggle();
  });

  // Send message (ONLY save to Firebase, don't append directly)
  $("#sendBtn").click(function () {
    let msg = $("#chatInput").val();
    if (msg.trim() !== "") {
      db.ref("messages").push({
        text: msg,
        timestamp: Date.now()
      });
      $("#chatInput").val("");
    }
    // Local auto-reply
    $("#chatBox").append(
      "<div class='systemMsg'>Your chat has been saved. We will get back to you in a moment.</div>"
    );
  });

  // Display messages from Firebase
  db.ref("messages").on("child_added", function (snapshot) {
    let msg = snapshot.val().text;
    $("#chatBox").append("<div class='serverMsg'>" + msg + "</div>");
    $("#chatBox").scrollTop($("#chatBox")[0].scrollHeight);
  });

  // Enter key = send
  $("#chatInput").keypress(function (e) {
    if (e.which == 13) {
      $("#sendBtn").click();
    }
  });
});
