from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).resolve().parents[2]
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


def main() -> None:
    TEXTURE_DIR.mkdir(parents=True, exist_ok=True)

    body = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(body)
    # Short metal needle, dark collar, white rear cap and two-pixel tail fins.
    draw.line((0, 7, 5, 7), fill="#D9E4E6")
    draw.line((1, 8, 5, 8), fill="#87979B")
    draw.rectangle((5, 6, 6, 9), fill="#252B2E")
    draw.rectangle((7, 6, 12, 9), outline="#252B2E")
    draw.rectangle((13, 6, 14, 9), fill="#E9E8E2")
    draw.point((15, 5), fill="#6D777B")
    draw.point((15, 10), fill="#6D777B")
    draw.line((14, 7, 15, 6), fill="#252B2E")
    draw.line((14, 8, 15, 9), fill="#252B2E")

    fluid = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    fluid_draw = ImageDraw.Draw(fluid)
    # Pure white tint mask: the item color handler replaces this with the
    # carried ampoule's exact medicine color.
    fluid_draw.rectangle((7, 7, 12, 8), fill="#FFFFFF")
    fluid_draw.point((8, 6), fill="#FFFFFF")

    body.save(TEXTURE_DIR / "injection_dart_body.png", optimize=True)
    fluid.save(TEXTURE_DIR / "injection_dart_fluid.png", optimize=True)


if __name__ == "__main__":
    main()
