import Notify from "quasar/src/plugins/Notify";

const sendError = message => {
  Notify.create({
    message: message,
    color: "red-5",
    textColor: "white",
    icon: "warning"
  });
  console.log("Error: " + message);
};

const sendSuccess = message => {
  Notify.create({
    message: message,
    color: "green-4",
    textColor: "white",
    icon: "cloud_done"
  });
};

export { sendError, sendSuccess };
