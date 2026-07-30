const fs = require("fs");
const path = require("path");
const zlib = require("zlib");

const projectRoot = path.resolve(__dirname, "..");
const outputDirectory = path.join(
    projectRoot,
    "src/main/resources/assets/syringe_mod/textures/gui/recipe");

const WIDTH = 118;
const HEIGHT = 84;
const colors = {
    transparent: [0, 0, 0, 0],
    enamelLight: [228, 229, 225, 255],
    enamelMid: [190, 195, 194, 255],
    enamelShadow: [129, 138, 142, 255],
    panel: [52, 60, 65, 255],
    dark: [23, 29, 33, 255],
    inset: [36, 43, 47, 255],
    rotor: [81, 93, 98, 255],
    cyan: [32, 187, 197, 255],
    cyanLight: [119, 216, 215, 255],
    cyanDark: [18, 108, 116, 255],
    purple: [130, 66, 167, 255],
    orange: [212, 111, 43, 255],
    green: [84, 154, 67, 255],
};

function createCanvas() {
    return {
        width: WIDTH,
        height: HEIGHT,
        data: Buffer.alloc(WIDTH * HEIGHT * 4),
    };
}

function pixel(image, x, y, color) {
    if (x < 0 || y < 0 || x >= image.width || y >= image.height) return;
    const offset = (y * image.width + x) * 4;
    image.data[offset] = color[0];
    image.data[offset + 1] = color[1];
    image.data[offset + 2] = color[2];
    image.data[offset + 3] = color[3];
}

function rectangle(image, left, top, right, bottom, color) {
    for (let y = top; y < bottom; y++) {
        for (let x = left; x < right; x++) {
            pixel(image, x, y, color);
        }
    }
}

function bevel(image, left, top, right, bottom) {
    rectangle(image, left, top, right, bottom, colors.enamelShadow);
    rectangle(image, left + 2, top + 2, right - 2, bottom - 2, colors.enamelLight);
    rectangle(image, left + 4, top + 4, right - 4, bottom - 4, colors.enamelMid);
}

function slotBay(image, x, y, marker) {
    bevel(image, x - 3, y - 3, x + 21, y + 21);
    rectangle(image, x, y, x + 18, y + 18, colors.dark);
    rectangle(image, x - 1, y + 2, x + 1, y + 16, marker);
}

function drawMixing() {
    const image = createCanvas();
    bevel(image, 0, 0, WIDTH, HEIGHT);
    rectangle(image, 4, 4, WIDTH - 4, HEIGHT - 4, colors.enamelLight);

    slotBay(image, 9, 13, colors.purple);
    slotBay(image, 9, 51, colors.cyan);
    rectangle(image, 29, 21, 39, 24, colors.panel);
    rectangle(image, 31, 22, 39, 23, colors.cyanDark);
    rectangle(image, 29, 59, 39, 62, colors.panel);
    rectangle(image, 31, 60, 39, 61, colors.cyanDark);

    bevel(image, 38, 6, 91, 73);
    rectangle(image, 42, 10, 87, 69, colors.panel);
    rectangle(image, 46, 14, 83, 65, colors.dark);

    const cx = 64;
    const cy = 39;
    rectangle(image, cx - 17, cy - 2, cx + 17, cy + 2, colors.rotor);
    rectangle(image, cx - 2, cy - 17, cx + 2, cy + 17, colors.rotor);
    rectangle(image, cx - 5, cy - 5, cx + 5, cy + 5, colors.enamelShadow);
    rectangle(image, cx - 2, cy - 2, cx + 2, cy + 2, colors.dark);
    rectangle(image, cx - 4, cy - 20, cx + 4, cy - 12, colors.cyan);
    rectangle(image, cx + 12, cy - 4, cx + 20, cy + 4, colors.cyan);
    rectangle(image, cx - 4, cy + 12, cx + 4, cy + 20, colors.purple);
    rectangle(image, cx - 20, cy - 4, cx - 12, cy + 4, colors.purple);

    rectangle(image, 91, 37, 99, 41, colors.panel);
    rectangle(image, 93, 38, 99, 40, colors.cyanDark);
    slotBay(image, 97, 31, colors.cyan);
    rectangle(image, 42, 74, 88, 80, colors.panel);
    rectangle(image, 45, 76, 75, 78, colors.cyan);
    return image;
}

function drawCrafting() {
    const image = createCanvas();
    bevel(image, 0, 0, WIDTH, HEIGHT);
    rectangle(image, 4, 4, WIDTH - 4, HEIGHT - 4, colors.enamelLight);

    slotBay(image, 7, 13, colors.orange);
    slotBay(image, 34, 13, colors.cyan);
    slotBay(image, 61, 13, colors.green);
    rectangle(image, 28, 20, 34, 24, colors.panel);
    rectangle(image, 29, 21, 34, 23, colors.cyanDark);
    rectangle(image, 55, 20, 61, 24, colors.panel);
    rectangle(image, 56, 21, 61, 23, colors.cyanDark);

    bevel(image, 23, 43, 90, 75);
    rectangle(image, 27, 47, 86, 71, colors.panel);
    rectangle(image, 31, 51, 82, 67, colors.dark);
    rectangle(image, 34, 58, 79, 65, colors.cyanDark);
    rectangle(image, 36, 56, 77, 63, colors.cyan);
    rectangle(image, 38, 55, 75, 58, colors.cyanLight);

    rectangle(image, 82, 20, 98, 24, colors.panel);
    rectangle(image, 85, 21, 98, 23, colors.cyanDark);
    slotBay(image, 97, 13, colors.cyan);
    rectangle(image, 94, 44, 112, 50, colors.panel);
    rectangle(image, 97, 46, 108, 48, colors.cyan);
    return image;
}

const crcTable = (() => {
    const table = new Uint32Array(256);
    for (let index = 0; index < 256; index++) {
        let value = index;
        for (let bit = 0; bit < 8; bit++) {
            value = (value & 1) !== 0
                ? 0xEDB88320 ^ (value >>> 1)
                : value >>> 1;
        }
        table[index] = value >>> 0;
    }
    return table;
})();

function crc32(buffer) {
    let value = 0xFFFFFFFF;
    for (const byte of buffer) {
        value = crcTable[(value ^ byte) & 0xFF] ^ (value >>> 8);
    }
    return (value ^ 0xFFFFFFFF) >>> 0;
}

function pngChunk(type, data) {
    const typeBuffer = Buffer.from(type, "ascii");
    const length = Buffer.alloc(4);
    length.writeUInt32BE(data.length);
    const checksum = Buffer.alloc(4);
    checksum.writeUInt32BE(crc32(Buffer.concat([typeBuffer, data])));
    return Buffer.concat([length, typeBuffer, data, checksum]);
}

function encodePng(image) {
    const header = Buffer.alloc(13);
    header.writeUInt32BE(image.width, 0);
    header.writeUInt32BE(image.height, 4);
    header[8] = 8;
    header[9] = 6;
    header[10] = 0;
    header[11] = 0;
    header[12] = 0;

    const stride = image.width * 4;
    const raw = Buffer.alloc((stride + 1) * image.height);
    for (let row = 0; row < image.height; row++) {
        const targetOffset = row * (stride + 1);
        raw[targetOffset] = 0;
        image.data.copy(
            raw,
            targetOffset + 1,
            row * stride,
            (row + 1) * stride);
    }

    return Buffer.concat([
        Buffer.from([137, 80, 78, 71, 13, 10, 26, 10]),
        pngChunk("IHDR", header),
        pngChunk("IDAT", zlib.deflateSync(raw, { level: 9 })),
        pngChunk("IEND", Buffer.alloc(0)),
    ]);
}

fs.mkdirSync(outputDirectory, { recursive: true });
fs.writeFileSync(
    path.join(outputDirectory, "potion_mixing.png"),
    encodePng(drawMixing()));
fs.writeFileSync(
    path.join(outputDirectory, "potion_crafting.png"),
    encodePng(drawCrafting()));

console.log(JSON.stringify({
    outputDirectory,
    textures: ["potion_mixing.png", "potion_crafting.png"],
    size: [WIDTH, HEIGHT],
}, null, 2));
