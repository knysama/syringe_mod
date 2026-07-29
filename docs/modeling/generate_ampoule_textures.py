from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[2]
TEXTURE_DIR = ROOT / "src/main/resources/assets/syringe_mod/textures/item"
PREVIEW_DIR = ROOT / "docs/concepts/ampoule_texture"

TRANSPARENT = (0, 0, 0, 0)

# These three colors are the liquid ramp in the user-approved 16x16 source
# sprite. Geometry, glass, stopper, highlights, and the X marking must remain
# byte-for-byte faithful to those source sprites.
SOURCE_LIQUID_RAMP = (
    (20, 92, 90, 255),
    (79, 154, 131, 255),
    (167, 207, 174, 255),
)

PALETTES = {
    "cure_injection": ((93, 73, 68), (169, 121, 108), (221, 183, 161)),
    "stem_cell_injection": ((21, 75, 63), (52, 119, 97), (123, 185, 154)),
    "cell_repair_injection": ((74, 64, 88), (121, 105, 138), (181, 164, 195)),
    "saturation_metabolism_injection": ((79, 71, 38), (128, 117, 68), (189, 177, 111)),
    "surge_injection": ((20, 92, 90), (79, 154, 131), (167, 207, 174)),
    "resistance_injection": ((53, 71, 89), (96, 116, 135), (160, 176, 188)),
    "immunity_enhancement_injection": ((57, 74, 54), (102, 123, 88), (167, 185, 145)),
    "experimental_injection": ((8, 11, 18), (23, 29, 43), (53, 64, 82)),
}

def render_shape(
        name: str,
        palette: tuple[tuple[int, int, int], ...],
        x_mark: bool = False) -> Image.Image:
    source_name = "x_ampoule_16x16.png" if x_mark else "ampoule_16x16.png"
    image = Image.open(PREVIEW_DIR / source_name).convert("RGBA")
    replacements = {
        source: (*target, 255)
        for source, target in zip(SOURCE_LIQUID_RAMP, palette)
    }
    image.putdata([replacements.get(pixel, pixel) for pixel in image.getdata()])
    image.save(TEXTURE_DIR / f"{name}.png")
    return image


def render_potion_layers() -> None:
    source = Image.open(PREVIEW_DIR / "ampoule_16x16.png").convert("RGBA")
    glass = source.copy()
    glass.putdata([
        TRANSPARENT if pixel in SOURCE_LIQUID_RAMP else pixel
        for pixel in source.getdata()
    ])
    glass.save(TEXTURE_DIR / "potion_ampoule_glass.png")

    liquid = Image.new("RGBA", (16, 16), TRANSPARENT)
    tint_ramp = {
        SOURCE_LIQUID_RAMP[0]: (142, 142, 142, 255),
        SOURCE_LIQUID_RAMP[1]: (210, 210, 210, 255),
        SOURCE_LIQUID_RAMP[2]: (255, 255, 255, 255),
    }
    liquid.putdata([tint_ramp.get(pixel, TRANSPARENT) for pixel in source.getdata()])
    liquid.save(TEXTURE_DIR / "potion_ampoule_liquid.png")


def render_joja_cola() -> Image.Image:
    rows = [
        "................",
        "................",
        ".....OOOOOO.....",
        "....OMMMMMMO....",
        "....OBLLLLBO....",
        "....OBLbbbBO....",
        "....OBbbbWBO....",
        "....OBbbbWBO....",
        "....OBbbbWBO....",
        "....OBWbbWBO....",
        "....OBbWWbBO....",
        "....OBLbbbBO....",
        "....OBBBBBBO....",
        "....OMMMMMMO....",
        ".....OOOOOO.....",
        "................",
    ]
    colors = {
        ".": TRANSPARENT,
        "O": (19, 30, 48, 255),
        "M": (139, 164, 184, 255),
        "B": (16, 83, 170, 255),
        "b": (24, 118, 219, 255),
        "L": (68, 153, 230, 255),
        "W": (226, 241, 242, 255),
    }
    image = Image.new("RGBA", (16, 16), TRANSPARENT)
    image.putdata([colors[pixel] for row in rows for pixel in row])
    image.save(TEXTURE_DIR / "joja_cola.png")
    return image


def render_gun_fluid_mask() -> None:
    fluid = Image.new("RGBA", (16, 16), (210, 210, 210, 255))
    pixels = fluid.load()
    for x in range(16):
        pixels[x, 0] = (255, 255, 255, 255)
        pixels[x, 15] = (142, 142, 142, 255)
    for y in range(1, 15):
        pixels[0, y] = (238, 238, 238, 255)
        pixels[15, y] = (168, 168, 168, 255)
    fluid.save(TEXTURE_DIR / "injection_gun_fluid.png")


def render_gun_glass_cutout() -> None:
    """Keep the chamber readable without hiding the tinted liquid volume."""
    glass = Image.new("RGBA", (16, 16), TRANSPARENT)
    pixels = glass.load()
    dark = (126, 157, 166, 255)
    light = (218, 238, 240, 255)
    for x in range(16):
        pixels[x, 0] = dark
        pixels[x, 15] = dark
    for y in range(16):
        pixels[0, y] = dark
        pixels[15, y] = dark
    for x in range(2, 7):
        pixels[x, 2] = light
    for y in range(3, 8):
        pixels[2, y] = light
    pixels[13, 13] = light
    glass.save(TEXTURE_DIR / "injection_gun_glass.png")


def render_preview(ampoules: list[Image.Image], joja: Image.Image) -> None:
    scale = 12
    cell = 16 * scale
    columns = len(ampoules) + 1
    preview = Image.new("RGBA", (cell * columns, cell), (126, 133, 144, 255))
    pixels = preview.load()
    checker = 8 * scale
    for y in range(preview.height):
        for x in range(preview.width):
            if (x // checker + y // checker) % 2:
                pixels[x, y] = (154, 160, 170, 255)
    for index, icon in enumerate(ampoules + [joja]):
        preview.alpha_composite(
            icon.resize((cell, cell), Image.Resampling.NEAREST),
            (index * cell, 0),
        )
    preview.save(PREVIEW_DIR / "ampoule_palette_preview.png")


def main() -> None:
    TEXTURE_DIR.mkdir(parents=True, exist_ok=True)
    PREVIEW_DIR.mkdir(parents=True, exist_ok=True)
    ampoules = [
        render_shape(
            name,
            palette,
            name == "experimental_injection")
        for name, palette in PALETTES.items()
    ]
    render_potion_layers()
    render_gun_fluid_mask()
    render_gun_glass_cutout()
    joja = render_joja_cola()
    render_preview(ampoules, joja)


if __name__ == "__main__":
    main()
