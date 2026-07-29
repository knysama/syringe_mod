from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[2]
SOURCE_DIR = (
    ROOT
    / "docs"
    / "concepts"
    / "medicine_bag"
    / "texture_sources"
)
TEXTURE_DIR = (
    ROOT
    / "src"
    / "main"
    / "resources"
    / "assets"
    / "syringe_mod"
    / "textures"
    / "item"
)
PREVIEW_PATH = (
    ROOT
    / "docs"
    / "concepts"
    / "medicine_bag"
    / "medicine_bag_item_textures_preview.png"
)

TRANSPARENT = (0, 0, 0, 0)
OUTLINE = "#171A1C"
GRAPHITE = "#292D30"
GRAPHITE_LIGHT = "#45494B"
SHELL_DARK = "#B9B8B2"
SHELL_SHADOW = "#D5D3CC"
SHELL = "#F1EFE8"
SHELL_LIGHT = "#FFFFFF"
CYAN_DARK = "#087984"
CYAN = "#28E3F0"


def new_sprite() -> tuple[Image.Image, ImageDraw.ImageDraw]:
    image = Image.new("RGBA", (16, 16), TRANSPARENT)
    return image, ImageDraw.Draw(image)


def paint_closed() -> Image.Image:
    image, draw = new_sprite()

    # Side belt loops.
    draw.rectangle((0, 6, 2, 10), fill=OUTLINE)
    draw.rectangle((1, 7, 1, 9), fill=GRAPHITE_LIGHT)
    draw.rectangle((13, 6, 15, 10), fill=OUTLINE)
    draw.rectangle((14, 7, 14, 9), fill=GRAPHITE_LIGHT)

    # Upright pouch body and thin closed top lid.
    draw.rectangle((2, 3, 13, 12), fill=OUTLINE)
    draw.rectangle((3, 2, 12, 2), fill=OUTLINE)
    draw.rectangle((3, 3, 12, 3), fill=SHELL_LIGHT)
    draw.rectangle((3, 4, 12, 4), fill=SHELL_SHADOW)
    draw.rectangle((3, 5, 12, 10), fill=SHELL)
    draw.rectangle((3, 11, 12, 11), fill=SHELL_SHADOW)
    draw.rectangle((3, 12, 12, 12), fill=SHELL_DARK)

    # Central latch with a small highlight; cyan status window at lower-right.
    draw.rectangle((6, 6, 9, 9), fill=SHELL_SHADOW)
    draw.rectangle((7, 5, 9, 9), fill=OUTLINE)
    draw.rectangle((8, 6, 8, 8), fill=GRAPHITE_LIGHT)
    draw.point((8, 9), fill=GRAPHITE)
    draw.rectangle((10, 9, 12, 11), fill=CYAN_DARK)
    draw.rectangle((11, 10, 11, 10), fill=CYAN)
    return image


def paint_open() -> Image.Image:
    image, draw = new_sprite()

    # Raised narrow top lid and its dark inner face.
    draw.rectangle((2, 0, 13, 6), fill=OUTLINE)
    draw.rectangle((3, 0, 12, 0), fill=SHELL_LIGHT)
    draw.rectangle((3, 1, 12, 1), fill=SHELL_SHADOW)
    draw.rectangle((3, 2, 12, 4), fill=GRAPHITE)
    draw.rectangle((4, 3, 11, 3), fill=GRAPHITE_LIGHT)
    draw.rectangle((3, 5, 4, 5), fill=OUTLINE)
    draw.rectangle((11, 5, 12, 5), fill=OUTLINE)

    # Side belt loops and permanent front wall.
    draw.rectangle((0, 8, 2, 12), fill=OUTLINE)
    draw.rectangle((1, 9, 1, 11), fill=GRAPHITE_LIGHT)
    draw.rectangle((13, 8, 15, 12), fill=OUTLINE)
    draw.rectangle((14, 9, 14, 11), fill=GRAPHITE_LIGHT)
    draw.rectangle((2, 6, 13, 14), fill=OUTLINE)

    # Five top-access ampoules. At 16×16, cap and liquid each receive one row.
    ampoule_colors = ("#D83227", "#E68B08", "#D8B31B", "#3B9B36", "#285BC4")
    for index, color in enumerate(ampoule_colors):
        left = 3 + index * 2
        draw.rectangle((left, 5, left + 1, 5), fill=SHELL_LIGHT)
        draw.rectangle((left, 6, left + 1, 6), fill=color)

    draw.rectangle((3, 7, 12, 7), fill=SHELL_SHADOW)
    draw.rectangle((3, 8, 12, 13), fill=SHELL)
    draw.rectangle((3, 14, 12, 14), fill=SHELL_DARK)

    # Same front details and relative placement as the closed sprite.
    draw.rectangle((6, 9, 9, 12), fill=SHELL_SHADOW)
    draw.rectangle((7, 8, 9, 12), fill=OUTLINE)
    draw.rectangle((8, 9, 8, 11), fill=GRAPHITE_LIGHT)
    draw.point((8, 12), fill=GRAPHITE)
    draw.rectangle((10, 11, 12, 13), fill=CYAN_DARK)
    draw.rectangle((11, 12, 11, 12), fill=CYAN)
    return image


def save_sprite(image: Image.Image, output_name: str) -> None:
    image.save(TEXTURE_DIR / output_name, optimize=True)


def approved_sprite(source_name: str, target_height: int, top: int) -> Image.Image:
    source = Image.open(SOURCE_DIR / source_name).convert("RGBA")
    # Chroma removal leaves a few near-transparent edge pixels around the
    # original canvas; ignore them when locating the actual approved sprite.
    alpha_box = source.getchannel("A").point(
        lambda value: 255 if value >= 128 else 0
    ).getbbox()
    if alpha_box is None:
        raise ValueError(f"Approved bag source is fully transparent: {source_name}")
    subject = source.crop(alpha_box)
    target_width = max(1, round(subject.width * target_height / subject.height))
    if target_width > 16:
        target_width = 16
        target_height = max(1, round(subject.height * target_width / subject.width))
    subject = subject.resize(
        (target_width, target_height),
        Image.Resampling.NEAREST,
    )
    sprite = Image.new("RGBA", (16, 16), TRANSPARENT)
    sprite.alpha_composite(subject, ((16 - target_width) // 2, top))
    return sprite


def main() -> None:
    TEXTURE_DIR.mkdir(parents=True, exist_ok=True)
    # The inventory icon is the previously approved open-bag concept (the
    # magenta background is not part of the sprite). Reconstructing it on the
    # native 16x16 grid preserves its intended hard pixel edges and colors;
    # shrinking the 1254px concept directly loses the ampoule colors.
    closed = paint_open()
    opened = paint_open()
    save_sprite(closed, "syringe_bag.png")
    save_sprite(opened, "syringe_bag_open.png")

    checker = Image.new("RGBA", (36, 18), (0, 0, 0, 0))
    checker_pixels = checker.load()
    for y in range(checker.height):
        for x in range(checker.width):
            value = 42 if (x + y) % 2 == 0 else 50
            checker_pixels[x, y] = (value, value, value, 255)

    checker.alpha_composite(closed, (1, 1))
    checker.alpha_composite(opened, (19, 1))
    checker.resize((576, 288), Image.Resampling.NEAREST).save(PREVIEW_PATH)


if __name__ == "__main__":
    main()
