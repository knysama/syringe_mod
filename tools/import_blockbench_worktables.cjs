const fs = require("fs");
const path = require("path");

const projectRoot = path.resolve(__dirname, "..");
const modelRoot = path.join(
    projectRoot, "src/main/resources/assets/syringe_mod/models/block");
const checkRoot = path.join(projectRoot, "build/model-import-check");

function readJson(file) {
    return JSON.parse(fs.readFileSync(file, "utf8"));
}

function writeJson(file, value) {
    fs.writeFileSync(file, `${JSON.stringify(value, null, 2)}\n`, "utf8");
}

function preserveProjectMetadata(exported, current) {
    if (!exported.display && current.display) {
        exported.display = current.display;
    }
    if (current.render_type) {
        exported.render_type = current.render_type;
    }
}

function normalizeJavaBlockRotations(model) {
    const supportedAngles = [-45, -22.5, 0, 22.5, 45];

    for (const element of model.elements ?? []) {
        const rotation = element.rotation;
        if (!rotation) {
            continue;
        }

        // Blockbench writes unsupported cube rotations (for example -50°)
        // as x/y/z components. Minecraft's block-model loader only accepts
        // one axis plus one of its five supported angles.
        if (!Number.isFinite(rotation.angle) || typeof rotation.axis !== "string") {
            const components = ["x", "y", "z"]
                .map(axis => [axis, rotation[axis]])
                .filter(([, angle]) => Number.isFinite(angle) && angle !== 0);

            if (components.length === 0) {
                delete element.rotation;
                continue;
            }
            if (components.length !== 1) {
                throw new Error(
                    `Element ${element.name ?? "<unnamed>"} rotates on multiple axes`);
            }

            const [axis, requestedAngle] = components[0];
            const angle = supportedAngles.reduce((nearest, candidate) =>
                Math.abs(candidate - requestedAngle) < Math.abs(nearest - requestedAngle)
                    ? candidate
                    : nearest);
            element.rotation = {
                angle,
                axis,
                origin: rotation.origin
            };
        }
    }
}

function importMixingTable() {
    const source = path.join(
        checkRoot, "mixing", "potion_mixing_table.json");
    const target = path.join(modelRoot, "potion_mixing_table.json");
    const exported = readJson(source);
    const current = readJson(target);

    for (const [key, texture] of Object.entries(exported.textures)) {
        exported.textures[key] =
            `syringe_mod:block/potion_mixing_table/${texture}`;
    }
    exported.textures.particle =
        "syringe_mod:block/potion_mixing_table/potion_mixing_table_casing";
    preserveProjectMetadata(exported, current);
    normalizeJavaBlockRotations(exported);
    exported.render_type = "minecraft:translucent";
    writeJson(target, exported);
}

function importCraftingTable() {
    const source = path.join(
        checkRoot, "crafting", "potion_crafting_table.json");
    const target = path.join(modelRoot, "potion_crafting_table.json");
    const exported = readJson(source);
    const current = readJson(target);

    for (const [key, texture] of Object.entries(exported.textures)) {
        if (key !== "particle") {
            exported.textures[key] = `syringe_mod:block/${texture}`;
        }
    }
    exported.textures.particle =
        "syringe_mod:block/potion_crafting_table_surface";
    preserveProjectMetadata(exported, current);
    normalizeJavaBlockRotations(exported);
    writeJson(target, exported);
}

importMixingTable();
importCraftingTable();
