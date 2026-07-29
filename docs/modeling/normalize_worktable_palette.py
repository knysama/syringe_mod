from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[2]
MIXING = (
    ROOT
    / "src/main/resources/assets/syringe_mod/textures/block/potion_mixing_table"
)

# Match the potion crafting table's warm enamel and navy panel exactly while
# retaining the mixing table's own pixel pattern and functional accent colors.
REMAPS = {
    "potion_mixing_table_casing.png": {
        (215, 218, 215): (215, 213, 207),
        (201, 206, 203): (197, 200, 196),
        (223, 226, 222): (224, 222, 216),
    },
    "potion_mixing_table_tabletop.png": {
        (228, 230, 226): (215, 213, 207),
        (217, 221, 216): (224, 222, 216),
        (233, 235, 231): (231, 229, 222),
    },
    "potion_mixing_table_panel.png": {
        (23, 28, 32): (32, 40, 51),
        (41, 49, 55): (43, 53, 67),
    },
}


def remap(path: Path, colors: dict[tuple[int, int, int], tuple[int, int, int]]) -> None:
    image = Image.open(path).convert("RGBA")
    pixels = image.load()
    for y in range(image.height):
        for x in range(image.width):
            red, green, blue, alpha = pixels[x, y]
            replacement = colors.get((red, green, blue))
            if replacement is not None:
                pixels[x, y] = (*replacement, alpha)
    image.save(path, optimize=True)


def main() -> None:
    for name, colors in REMAPS.items():
        remap(MIXING / name, colors)


if __name__ == "__main__":
    main()
