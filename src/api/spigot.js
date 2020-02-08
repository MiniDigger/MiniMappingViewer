import { sendError } from "src/api/notify";

const parseSpigot = (classes, members) => {
  let spigotToObf = {};
  let obfToSpigot = {};

  let parts = null;
  classes.split("\n").forEach(line => {
    if (line.startsWith("#") || line === "") {
      // comment
    } else {
      parts = line.split(" ");
      if (parts && parts.length === 2) {
        let obf = parts[0];
        let spigot = parts[1];

        spigotToObf[spigot] = {
          mapped: spigot,
          obf: obf,
          members: {}
        };
        obfToSpigot[obf] = {
          mapped: spigot,
          obf: obf,
          members: {}
        };
      } else {
        sendError("Invalid class line " + line);
      }
    }
  });

  // minecraft server has special handling
  spigotToObf["net.minecraft.server.MinecraftServer"] = {
    mapped: "net.minecraft.server.MinecraftServer",
    obf: "net.minecraft.server.MinecraftServer",
    members: {}
  };
  obfToSpigot["net.minecraft.server.MinecraftServer"] = {
    mapped: "net.minecraft.server.MinecraftServer",
    obf: "net.minecraft.server.MinecraftServer",
    members: {}
  };

  let currSpigot = null;
  // let currObf = null;

  members.split("\n").forEach(line => {
    if (line.startsWith("#") || line === "") {
      // comment
    } else {
      // for some reason minecraftserver is fully qualified?
      if (line.startsWith("net/minecraft/server/")) {
        line =
          "net.minecraft.server." +
          line.substring("net/minecraft/server/".length);
      }

      // method or field?
      if (line.indexOf("(") !== -1) {
        parts = line.split(" ");
        if (parts && parts.length === 4) {
          currSpigot = spigotToObf[parts[0]];

          let returnType = "";
          let params = "";

          if (currSpigot) {
            currSpigot.members[parts[2]] = {
              type: "method",
              returnType: returnType,
              mapped: parts[3],
              obf: parts[1],
              params: params
            };
          } else {
            sendError("error: didnt find (sm): " + parts[0]);
          }

          // currObf = obfToSpigot[parts[1]];
          // if (currObf) {
          //   currObf.members[parts[1]] = {
          //     type: "method",
          //     returnType: returnType,
          //     mapped: parts[3],
          //     obf: parts[1],
          //     params: params
          //   };
          // } else {
          //   // sendError("error: didnt find (om): " + parts[0]);
          // }
        } else {
          sendError("Invalid line (f): " + parts);
        }
      } else {
        parts = line.split(" ");
        if (parts && parts.length === 3) {
          currSpigot = spigotToObf[parts[0]];

          if (currSpigot) {
            currSpigot.members[parts[2]] = {
              type: "field",
              dataType: "",
              mapped: parts[2],
              obf: parts[1]
            };
          } else {
            sendError("error: didnt find (sf): " + parts[0]);
          }

          // currObf = obfToSpigot[parts[0]];
          //
          // if (currObf) {
          //   currObf.members[parts[1]] = {
          //     type: "field",
          //     dataType: "",
          //     mapped: parts[2],
          //     obf: parts[1]
          //   };
          // } else {
          //   sendError("error: didnt find (of): " + parts[0]);
          // }
        } else {
          sendError("Invalid line (f): " + parts);
        }
      }
    }
  });

  return {
    mappedToObf: spigotToObf,
    obfToMapped: obfToSpigot
  };
};

export { parseSpigot };
