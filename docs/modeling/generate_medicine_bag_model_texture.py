from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[2]
OUTPUT = (
    ROOT
    / "src"
    / "main"
    / "resources"
    / "assets"
    / "syringe_mod"
    / "textures"
    / "item"
    / "syringe_bag_model.png"
)

COLORS = {
    "outline": "#171A1C",
    "graphite": "#292D30",
    "graphite_light": "#45494B",
    "shell_dark": "#8E8D88",
    "shell_shadow": "#B8B6AF",
    "shell": "#D9D6CD",
    "shell_light": "#EEEADF",
    "cyan_dark": "#087984",
    "cyan": "#18BED0",
    "red": "#D83227",
    "orange": "#E68B08",
    "yellow": "#D8B31B",
    "green": "#3B9B36",
    "blue": "#285BC4",
    "cavity": "#202427",
    "slot": "#363A3D",
}


def main() -> None:
    image = Image.new("RGBA", (32, 32), COLORS["shell"])
    draw = ImageDraw.Draw(image)

    # A: front panel, 12x7.
    draw.rectangle((0, 0, 11, 6), fill=COLORS["shell"])
    draw.line((0, 0, 11, 0), fill=COLORS["shell_light"])
    draw.line((0, 6, 11, 6), fill=COLORS["shell_shadow"])
    draw.line((0, 1, 0, 5), fill=COLORS["shell_shadow"])
    draw.line((11, 1, 11, 5), fill=COLORS["shell_dark"])

    # B: rear panel, 12x7.
    draw.rectangle((0, 7, 11, 13), fill=COLORS["shell"])
    draw.line((0, 7, 11, 7), fill=COLORS["shell_light"])
    draw.line((0, 13, 11, 13), fill=COLORS["shell_shadow"])

    # C: lid exterior, 12x4, with restrained segmented ribs.
    draw.rectangle((0, 14, 11, 17), fill=COLORS["shell"])
    draw.line((0, 14, 11, 14), fill=COLORS["shell_light"])
    draw.line((0, 17, 11, 17), fill=COLORS["shell_shadow"])
    for x in (3, 6, 9):
        draw.point((x, 15), fill=COLORS["shell_shadow"])
        draw.point((x, 16), fill=COLORS["shell_shadow"])

    # D: lid interior, 12x4.
    draw.rectangle((0, 18, 11, 21), fill=COLORS["graphite"])
    draw.line((0, 18, 11, 18), fill=COLORS["graphite_light"])
    draw.line((0, 21, 11, 21), fill=COLORS["outline"])

    # E/F/G: side shell, cavity floor, and trim strip.
    draw.rectangle((12, 0, 15, 6), fill=COLORS["shell_shadow"])
    draw.line((12, 0, 15, 0), fill=COLORS["shell_light"])
    draw.line((15, 1, 15, 6), fill=COLORS["shell_dark"])
    draw.rectangle((12, 7, 21, 8), fill=COLORS["cavity"])
    draw.line((12, 7, 21, 7), fill=COLORS["graphite_light"])
    draw.rectangle((12, 9, 23, 9), fill=COLORS["shell_shadow"])

    # J: compact five-ampoule strip used by UV inspection and reuse.
    ampoule_colors = (
        COLORS["red"],
        COLORS["orange"],
        COLORS["yellow"],
        COLORS["green"],
        COLORS["blue"],
    )
    for index, color in enumerate(ampoule_colors):
        x = index * 2
        draw.rectangle((x, 23, x + 1, 23), fill=COLORS["shell_light"])
        draw.rectangle((x, 24, x + 1, 25), fill=color)

    # Palette swatches. Each cube category can safely stretch one integer texel.
    swatch_order = (
        "outline",
        "graphite",
        "graphite_light",
        "shell_dark",
        "shell_shadow",
        "shell",
        "shell_light",
        "cyan_dark",
        "cyan",
        "red",
        "orange",
        "yellow",
        "green",
        "blue",
        "cavity",
        "slot",
    )
    for x, name in enumerate(swatch_order):
        draw.point((x, 31), fill=COLORS[name])

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    image.save(OUTPUT, optimize=True)


if __name__ == "__main__":
    main()
