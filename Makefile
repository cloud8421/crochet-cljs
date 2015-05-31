.PHONY: all watch-scss build
FSWATCH       := fswatch
SASSC         := sassc

STYLES        := styles/
MAIN_STYLE    := $(STYLES)style.scss
OUT_STYLE     := resources/public/css/style.css
RELEASE_STYLE := dist/style-min.css

MAIN_HTML     := index_release.html
RELEASE_HTML  := dist/index.html

all: watch-scss

build: build-cljs build-scss build-html

watch-scss:
	$(FSWATCH) --recursive --one-per-batch $(STYLES) | xargs -n1 -I{} $(SASSC) $(MAIN_STYLE) $(OUT_STYLE)

build-cljs:
	lein cljsbuild once min

build-scss:
	$(SASSC) -t compressed $(MAIN_STYLE) $(RELEASE_STYLE)

build-html:
	cp $(MAIN_HTML) $(RELEASE_HTML)
