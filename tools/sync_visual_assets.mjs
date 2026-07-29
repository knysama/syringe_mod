import fs from "node:fs";
import path from "node:path";

const root = path.resolve(import.meta.dirname, "..");

function readJson(relativePath) {
  return JSON.parse(fs.readFileSync(path.join(root, relativePath), "utf8"));
}

function writeJson(relativePath, value) {
  fs.writeFileSync(
    path.join(root, relativePath),
    `${JSON.stringify(value, null, 2)}\n`,
    "utf8",
  );
}

function normalizeBagElement(element) {
  const normalized = structuredClone(element);
  for (const face of Object.values(normalized.faces ?? {})) {
    if (face.texture === "#2") {
      face.texture = "#0";
    }
  }
  return normalized;
}

function syncMedicineBag() {
  const source = readJson(
    "docs/modeling/blockbench_export/syringe_bag_current_user_sync_20260729/syringe_bag_model.json",
  );
  const byName = new Map(source.elements.map((element) => [element.name, element]));

  const bodyNames = [
    "front_panel",
    "back_panel",
    "left_wall",
    "right_wall",
    "cavity_floor",
    "bottom_front_trim",
    "top_front_lip",
    "left_front_trim",
    "right_front_trim",
    "hinge_body_left",
    "hinge_body_right",
    "left_loop_top",
    "left_loop_bottom",
    "left_loop_outer",
    "right_loop_top",
    "right_loop_bottom",
    "right_loop_outer",
    "latch_backplate",
    "latch_housing",
    "latch_inset",
    "latch_lower_lip",
    "indicator_frame",
    "indicator_glass",
  ];
  const lidNames = [
    "lid_shell_cube",
    "lid_inner_panel",
    "hinge_lid_left",
    "hinge_lid_right",
  ];
  const ampouleNames = source.elements
    .filter((element) => /^(slot_|ampoule_)/.test(element.name))
    .map((element) => element.name);

  const writePart = (fileName, names) => {
    const missing = names.filter((name) => !byName.has(name));
    if (missing.length > 0) {
      throw new Error(`Missing Blockbench elements: ${missing.join(", ")}`);
    }
    writeJson(`src/main/resources/assets/syringe_mod/models/item/${fileName}`, {
      textures: {
        0: "syringe_mod:item/syringe_bag_model",
        particle: "syringe_mod:item/syringe_bag_model",
      },
      elements: names.map((name) => normalizeBagElement(byName.get(name))),
    });
  };

  writePart("syringe_bag_3d_body.json", bodyNames);
  writePart("syringe_bag_3d_lid.json", lidNames);
  writePart("syringe_bag_3d_ampoules.json", ampouleNames);

  fs.copyFileSync(
    path.join(
      root,
      "docs/modeling/blockbench_export/syringe_bag_current_user_sync_20260729/syringe_bag_model.png",
    ),
    path.join(
      root,
      "src/main/resources/assets/syringe_mod/textures/item/syringe_bag_model.png",
    ),
  );
}

function correctedTableDisplay() {
  return {
    thirdperson_righthand: {
      rotation: [30, 180, 0],
      scale: [0.3, 0.3, 0.3],
    },
    thirdperson_lefthand: {
      rotation: [30, 180, 0],
      scale: [0.3, 0.3, 0.3],
    },
    firstperson_righthand: {
      rotation: [20, 180, 0],
      translation: [0, 1, 0],
      scale: [0.4, 0.4, 0.4],
    },
    firstperson_lefthand: {
      rotation: [20, 180, 0],
      translation: [0, 1, 0],
      scale: [0.4, 0.4, 0.4],
    },
    gui: {
      rotation: [20, -134, 0],
      scale: [0.7, 0.7, 0.7],
    },
    fixed: {
      rotation: [0, 180, 0],
      translation: [0, 0, -4.25],
    },
  };
}

function syncWorktableDisplays() {
  for (const fileName of [
    "potion_crafting_table.json",
    "potion_crafting_table_active.json",
    "potion_mixing_table.json",
    "potion_mixing_table_active.json",
  ]) {
    const relativePath = `src/main/resources/assets/syringe_mod/models/block/${fileName}`;
    const model = readJson(relativePath);
    model.display = correctedTableDisplay();
    writeJson(relativePath, model);
  }
}

function syncMixingLid() {
  const idlePath =
    "src/main/resources/assets/syringe_mod/models/block/potion_mixing_table.json";
  const activePath =
    "src/main/resources/assets/syringe_mod/models/block/potion_mixing_table_active.json";
  const idle = readJson(idlePath);
  const active = readJson(activePath);
  const closedLid = new Map(
    active.elements
      .filter(
        (element) =>
          element.name.startsWith("lid_") ||
          element.name.startsWith("gasket_"),
      )
      .map((element) => [element.name, element]),
  );
  const hinge = [8, 9.88, 13.05];

  idle.elements = idle.elements.map((element) => {
    if (
      !element.name.startsWith("lid_") &&
      !element.name.startsWith("gasket_")
    ) {
      return element;
    }
    const source = closedLid.get(element.name);
    if (!source) {
      throw new Error(`Missing closed lid element: ${element.name}`);
    }
    const opened = structuredClone(source);
    // Keep every lid piece in the same closed-space coordinates and rotate the
    // complete assembly around one hinge. The lid extends from the rear hinge
    // toward the centrifuge (-Z), so +45 degrees opens toward the centrifuge.
    opened.rotation = {
      angle: 45,
      axis: "x",
      origin: hinge,
    };
    return opened;
  });
  writeJson(idlePath, idle);
}

function syncInjectionGunChamberTint() {
  for (const fileName of [
    "injection_gun_empty.json",
    "injection_gun_loaded.json",
    "injection_gun_reload_1.json",
    "injection_gun_reload_2.json",
    "injection_gun_reload_3.json",
  ]) {
    const relativePath =
      `src/main/resources/assets/syringe_mod/models/item/${fileName}`;
    const model = readJson(relativePath);
    const chamber = model.elements.find(
      (element) => element.name === "empty_chamber_glass",
    );
    if (!chamber) {
      throw new Error(`Missing injection gun chamber in ${fileName}`);
    }
    const loaded = fileName !== "injection_gun_empty.json";
    for (const face of Object.values(chamber.faces ?? {})) {
      face.texture = loaded ? "#2" : "#1";
      if (loaded) {
        face.tintindex = 0;
      } else {
        delete face.tintindex;
      }
    }
    writeJson(relativePath, model);
  }
}

syncMedicineBag();
syncWorktableDisplays();
syncMixingLid();
syncInjectionGunChamberTint();
