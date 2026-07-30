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
  const hinge = [8, 9.88, 13.05];

  idle.elements = idle.elements.map((element) => {
    if (
      !element.name.startsWith("lid_") &&
      !element.name.startsWith("gasket_")
    ) {
      return element;
    }
    const opened = structuredClone(element);
    opened.rotation = {
      angle: 45,
      axis: "x",
      origin: hinge,
    };
    if (opened.name === "lid_glass_panel") {
      // Give the transparent pane enough physical depth that its front and
      // back faces do not fight in shallow viewing angles.
      opened.from[1] = 10.07;
      opened.to[1] = 10.27;
    }
    return opened;
  });

  // The processing model closes the lid. Keep the complete assembly above the
  // highest rotor sample (Y=10.28), with the glass separated from the gasket.
  const closedClearanceMinY = 10.42;
  const activeLidElements = active.elements.filter(
    (element) =>
      element.name.startsWith("lid_") ||
      element.name.startsWith("gasket_"),
  );
  const currentClosedMinY = Math.min(
    ...activeLidElements.map((element) => element.from[1]),
  );
  const closedOffsetY = closedClearanceMinY - currentClosedMinY;
  active.elements = active.elements.map((element) => {
    if (
      !element.name.startsWith("lid_") &&
      !element.name.startsWith("gasket_")
    ) {
      return element;
    }
    const closed = structuredClone(element);
    delete closed.rotation;
    closed.from[1] += closedOffsetY;
    closed.to[1] += closedOffsetY;
    if (closed.name === "lid_glass_panel") {
      closed.from[1] = 10.68;
      closed.to[1] = 10.88;
    }
    return closed;
  });

  writeJson(idlePath, idle);
  writeJson(activePath, active);
}

function syncMixingDepthSafety() {
  const stainTopByName = new Map([
    ["stain_purple_puddle_a", 8.032],
    ["stain_purple_puddle_b", 8.044],
    ["stain_purple_puddle_c", 8.056],
    ["stain_purple_tail", 8.068],
    ["stain_purple_drop_far", 8.08],
    ["stain_purple_drop_small", 8.092],
    ["stain_cyan_puddle_a", 8.032],
    ["stain_cyan_puddle_b", 8.044],
    ["stain_cyan_puddle_c", 8.056],
    ["stain_cyan_tail", 8.068],
    ["stain_cyan_drop_far", 8.08],
    ["stain_cyan_drop_left", 8.092],
    ["stain_cyan_drop_top", 8.104],
  ]);

  for (const fileName of [
    "potion_mixing_table.json",
    "potion_mixing_table_active.json",
  ]) {
    const relativePath =
      `src/main/resources/assets/syringe_mod/models/block/${fileName}`;
    const model = readJson(relativePath);
    const byName = new Map(
      model.elements.map((element) => [element.name, element]),
    );

    // Give nested trim pieces distinct exterior planes. The old coordinates
    // shared portions of the same plane and flickered on some depth buffers.
    const bottomPlinth = byName.get("bottom_plinth");
    bottomPlinth.from = [0.35, 0.75, 0.6];
    bottomPlinth.to = [15.65, 1.25, 15.15];

    for (const footName of [
      "foot_front_left",
      "foot_front_right",
      "foot_rear_left",
      "foot_rear_right",
    ]) {
      byName.get(footName).to[1] = 1.2;
    }
    byName.get("foot_rear_left").to[2] = 15.5;
    byName.get("foot_rear_right").to[2] = 15.5;

    const frontBand = byName.get("plinth_front_band");
    frontBand.from[0] = 3.2;
    frontBand.to[0] = 13.2;
    byName.get("plinth_right_band").to[2] = 13.2;

    // These faces are fully buried inside their adjoining parts.
    delete byName.get("tray_base").faces.down;
    const armRight = byName.get("arm_right");
    if (armRight) {
      delete armRight.faces.east;
    }

    // The irregular spill is assembled from overlapping top-only planes.
    // Stagger their heights deterministically so no two overlaps are coplanar.
    for (const [name, topY] of stainTopByName) {
      const element = byName.get(name);
      if (element) {
        element.to[1] = topY;
      }
    }

    writeJson(relativePath, model);
  }
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

function sightElement(name, from, to) {
  const faces = {};
  for (const direction of ["north", "east", "south", "west", "up", "down"]) {
    faces[direction] = {
      uv: [0, 0, Math.max(1, to[0] - from[0]), Math.max(1, to[1] - from[1])],
      texture: "#5",
    };
  }
  return {
    name,
    from,
    to,
    faces,
  };
}

function syncInjectionGunSights() {
  const sightNames = new Set([
    "front_sight_post",
    "rear_sight_left",
    "rear_sight_right",
  ]);
  const sights = [
    sightElement("front_sight_post", [5.0, 15.0, 7.55], [5.8, 16.35, 8.45]),
    sightElement("rear_sight_left", [18.0, 15.0, 6.25], [19.0, 16.25, 7.35]),
    sightElement("rear_sight_right", [18.0, 15.0, 8.65], [19.0, 16.25, 9.75]),
  ];

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
    model.elements = model.elements.filter(
      (element) => !sightNames.has(element.name),
    );
    model.elements.push(...structuredClone(sights));
    writeJson(relativePath, model);
  }
}

syncMedicineBag();
syncWorktableDisplays();
syncMixingLid();
syncMixingDepthSafety();
syncInjectionGunChamberTint();
syncInjectionGunSights();
