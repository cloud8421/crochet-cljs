.PHONY: styles

styles:
	sassc styles/style.scss out/styles/style.css

all:
	styles
