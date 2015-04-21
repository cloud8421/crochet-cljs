.PHONY: all server watch watch-scss build
FSWATCH       := fswatch
SASSC         := sassc
STYLES        := styles/
MAIN_STYLE    := $(STYLES)style.scss
OUT_STYLE     := out/styles/style.css
RELEASE_STYLE := release/styles/style-min.css

all: server watch watch-scss

server:
	python -m SimpleHTTPServer > /dev/null 2>&1

watch:
	./scripts/watch

watch-scss:
	$(FSWATCH) --recursive --one-per-batch $(STYLES) | xargs -n1 -I{} $(SASSC) $(MAIN_STYLE) $(OUT_STYLE)

build:
	./scripts/release
	mkdir -p release/styles && $(SASSC) -t compressed $(MAIN_STYLE) $(RELEASE_STYLE)
	cp index_release.html release/index.html
