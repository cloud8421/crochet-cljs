.PHONY: all watch-scss build
FSWATCH       := fswatch
SASSC         := sassc
STYLES        := styles/
MAIN_STYLE    := $(STYLES)style.scss
OUT_STYLE     := resources/public/css/style.css
RELEASE_STYLE := resources/public/css/style-min.css

all: watch-scss

watch-scss:
	$(FSWATCH) --recursive --one-per-batch $(STYLES) | xargs -n1 -I{} $(SASSC) $(MAIN_STYLE) $(OUT_STYLE)

build:
	lein cljsbuild once min
	$(SASSC) -t compressed $(MAIN_STYLE) $(RELEASE_STYLE)
	cp index_release.html release/index.html
