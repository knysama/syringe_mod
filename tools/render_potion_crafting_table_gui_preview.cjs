const fs = require("fs");
const path = require("path");
const { PNG } = require("pngjs");

const projectRoot = path.resolve(__dirname, "..");
const textureRoot = path.join(
    projectRoot,
    "src/main/resources/assets/autoforge_bricks/textures/gui");
const outputDirectory = path.join(
    projectRoot,
    "docs/references/potion_crafting_table");
const outputPath = path.join(outputDirectory, "potion_crafting_table_gui_preview.png");

const WIDTH = 176;
const HEIGHT = 190;
const SCALE = 4;
const image = new PNG({ width: WIDTH, height: HEIGHT, colorType: 6 });
image.data.fill(0);

const colors = {
    enamelLight: 0xFFD9D7D0,
    enamelMid: 0xFFB8B7B2,
    enamelShadow: 0xFF85898A,
    panelDark: 0xFF252B30,
    panelInset: 0xFF171C20,
    cyan: 0xFF20B8C7,
    cyanDark: 0xFF126A73,
    heat: 0xFFE47728
};

function rgba(argb) {
    return [
        (argb >>> 16) & 0xFF,
        (argb >>> 8) & 0xFF,
        argb & 0xFF,
        (argb >>> 24) & 0xFF
    ];
}

function setPixel(target, x, y, color) {
    if (x < 0 || y < 0 || x >= target.width || y >= target.height) return;
    const index = (y * target.width + x) * 4;
    target.data[index] = color[0];
    target.data[index + 1] = color[1];
    target.data[index + 2] = color[2];
    target.data[index + 3] = color[3];
}

function fill(left, top, right, bottom, argb) {
    const color = rgba(argb);
    for (let y = top; y < bottom; y++) {
        for (let x = left; x < right; x++) {
            setPixel(image, x, y, color);
        }
    }
}

function load(name) {
    return PNG.sync.read(fs.readFileSync(path.join(textureRoot, `${name}.png`)));
}

function blit(source, dx, dy, width = source.width, height = source.height) {
    for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
            const sx = Math.floor(x * source.width / width);
            const sy = Math.floor(y * source.height / height);
            const sourceIndex = (sy * source.width + sx) * 4;
            const alpha = source.data[sourceIndex + 3] / 255;
            if (alpha === 0) continue;
            const targetX = dx + x;
            const targetY = dy + y;
            if (targetX < 0 || targetY < 0 || targetX >= WIDTH || targetY >= HEIGHT) continue;
            const targetIndex = (targetY * WIDTH + targetX) * 4;
            for (let channel = 0; channel < 3; channel++) {
                image.data[targetIndex + channel] = Math.round(
                    source.data[sourceIndex + channel] * alpha
                    + image.data[targetIndex + channel] * (1 - alpha));
            }
            image.data[targetIndex + 3] = 255;
        }
    }
}

function panel(left, top, right, bottom) {
    fill(left, top, right, bottom, colors.enamelShadow);
    fill(left + 2, top + 2, right - 2, bottom - 2, colors.enamelLight);
    fill(left + 4, top + 4, right - 4, bottom - 4, colors.enamelMid);
}

const fillTexture = load("fill_white");
const topLeft = load("border_corner_tl");
const topRight = load("border_corner_tr");
const bottomLeft = load("border_corner_bl");
const bottomRight = load("border_corner_br");
const edgeTop = load("border_edge_top");
const edgeBottom = load("border_edge_bottom");
const edgeLeft = load("border_edge_left");
const edgeRight = load("border_edge_right");
const slotTexture = load("slot_default");
const splitTexture = load("split_bar");
const playerSlots = load("player_slots_9x4");

blit(fillTexture, 5, 5, WIDTH - 10, HEIGHT - 10);
blit(topLeft, 0, 0);
blit(topRight, WIDTH - 5, 0);
blit(bottomLeft, 0, HEIGHT - 5);
blit(bottomRight, WIDTH - 5, HEIGHT - 5);
blit(edgeTop, 5, 0, WIDTH - 10, 5);
blit(edgeBottom, 5, HEIGHT - 5, WIDTH - 10, 5);
blit(edgeLeft, 0, 5, 5, HEIGHT - 10);
blit(edgeRight, WIDTH - 5, 5, 5, HEIGHT - 10);

panel(9, 21, WIDTH - 9, 87);
fill(13, 25, 94, 83, colors.panelDark);
fill(96, 25, 136, 83, colors.panelDark);
fill(138, 25, WIDTH - 13, 83, colors.panelDark);

const inputSlots = [
    { x: 20, y: 43, marker: 0xFFCA5A32 },
    { x: 46, y: 43, marker: 0xFFD2AE38 },
    { x: 72, y: 43, marker: 0xFF5AA447 }
];
for (const slot of inputSlots) {
    fill(slot.x - 3, 30, slot.x + 19, 66, colors.panelInset);
    fill(slot.x - 1, 31, slot.x + 17, 34, slot.marker);
    blit(slotTexture, slot.x - 1, slot.y - 1);
    fill(slot.x + 7, 64, slot.x + 9, 72, colors.enamelShadow);
}

fill(28, 72, 82, 75, colors.enamelShadow);
fill(80, 69, 99, 72, colors.enamelShadow);
fill(94, 67, 99, 74, colors.enamelShadow);

const progress = 55;
const potX = 101;
const potY = 34;
const potWidth = 30;
const liquidHeight = Math.floor(17 * progress / 100);
fill(potX - 3, potY + 3, potX, potY + 7, colors.enamelShadow);
fill(potX + potWidth, potY + 3, potX + potWidth + 3, potY + 7, colors.enamelShadow);
fill(potX, potY, potX + potWidth, potY + 4, colors.enamelShadow);
fill(potX + 3, potY + 4, potX + potWidth - 3, potY + 23, colors.panelInset);
fill(potX + 5, potY + 21 - liquidHeight, potX + potWidth - 5, potY + 21, colors.cyan);
fill(potX + 5, potY + 21 - liquidHeight, potX + potWidth - 5, potY + 23 - liquidHeight,
    0xFF75D1D8);
fill(potX + 5, potY + 25, potX + potWidth - 5, potY + 29, 0xFF442A21);
fill(potX + 5, potY + 25, potX + 5 + Math.floor(20 * progress / 100), potY + 29,
    colors.heat);

fill(132, 48, 141, 51, colors.cyanDark);
fill(138, 45, 141, 54, colors.cyanDark);
fill(141, 39, 165, 63, colors.cyanDark);
blit(slotTexture, 144, 42);
fill(145, 70, 161, 74, colors.panelInset);
fill(147, 71, 159, 73, colors.cyan);

blit(splitTexture, 5, 94, WIDTH - 10, 14);
blit(playerSlots, 6, 108);

const scaled = new PNG({
    width: WIDTH * SCALE,
    height: HEIGHT * SCALE,
    colorType: 6
});
for (let y = 0; y < scaled.height; y++) {
    for (let x = 0; x < scaled.width; x++) {
        const sourceX = Math.floor(x / SCALE);
        const sourceY = Math.floor(y / SCALE);
        const sourceIndex = (sourceY * WIDTH + sourceX) * 4;
        const targetIndex = (y * scaled.width + x) * 4;
        for (let channel = 0; channel < 4; channel++) {
            scaled.data[targetIndex + channel] = image.data[sourceIndex + channel];
        }
    }
}

fs.mkdirSync(outputDirectory, { recursive: true });
fs.writeFileSync(outputPath, PNG.sync.write(scaled));
console.log(outputPath);
