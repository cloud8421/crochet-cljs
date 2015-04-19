.PHONY: all styles server watch watch-scss
FSWATCH    := fswatch
SASSC      := sassc
STYLES     := styles/
MAIN_STYLE := $(STYLES)/style.scss
OUT_STYLE  := out/styles/style.css

all: server watch watch-scss

styles:
	sassc styles/style.scss out/styles/style.css

server:
	python -m SimpleHTTPServer > /dev/null 2>&1

watch:
	./scripts/watch

watch-scss:
	$(FSWATCH) --recursive --one-per-batch $(STYLES) | xargs -n1 -I{} $(SASSC) $(MAIN_STYLE) $(OUT_STYLE)
