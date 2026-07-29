import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const scriptDir = path.dirname(fileURLToPath(import.meta.url));
const projectRoot = path.resolve(scriptDir, "../../..");
const sourcePath = path.join(scriptDir, "syringe_bag_model.bbmodel");
const outputDir = path.join(
  projectRoot,
  "src/main/resources/assets/syringe_mod/models/item"
);

const source = JSON.parse(fs.readFileSync(sourcePath, "utf8"));
const ignoredNames = new Set(["__mcp_connection_test__"]);
const lidNames = new Set([
  "lid_shell_cube",
  "lid_inner_panel",
  "hinge_lid_left",
  "hinge_lid_right",
]);

function targetFor(element) {
  if (lidNames.has(element.name)) {
    return "lid";
  }
  if (element.name.startsWith("ampoule_") || element.name.startsWith("slot_")) {
    return "ampoules";
  }
  return "body";
}

function convertElement(element) {
  const faces = {};
  for (const [direction, face] of Object.entries(element.faces)) {
    if (face.texture === null) {
      continue;
    }
    faces[direction] = {
      uv: face.uv,
      texture: "#0",
    };
  }

  return {
    name: element.name,
    from: element.from,
    to: element.to,
    faces,
  };
}

const partitions = {
  body: [],
  lid: [],
  ampoules: [],
};

for (const element of source.elements) {
  if (element.type !== "cube" || ignoredNames.has(element.name) || !element.export) {
    continue;
  }
  partitions[targetFor(element)].push(convertElement(element));
}

fs.mkdirSync(outputDir, { recursive: true });

for (const [part, elements] of Object.entries(partitions)) {
  const model = {
    credit: "Generated from syringe_bag_model.bbmodel",
    texture_size: [32, 32],
    textures: {
      0: "syringe_mod:item/syringe_bag_model",
      particle: "syringe_mod:item/syringe_bag_model",
    },
    elements,
  };
  const outputPath = path.join(outputDir, `syringe_bag_3d_${part}.json`);
  fs.writeFileSync(outputPath, `${JSON.stringify(model, null, 2)}\n`);
}

const iconModel = {
  parent: "minecraft:item/generated",
  textures: {
    layer0: "syringe_mod:item/syringe_bag",
  },
};
fs.writeFileSync(
  path.join(outputDir, "syringe_bag_icon.json"),
  `${JSON.stringify(iconModel, null, 2)}\n`
);

for (const [part, elements] of Object.entries(partitions)) {
  process.stdout.write(`${part}: ${elements.length} cubes\n`);
}
