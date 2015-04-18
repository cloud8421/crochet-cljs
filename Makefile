.PHONY: styles server watch

styles:
	sassc styles/style.scss out/styles/style.css

server:
	python -m SimpleHTTPServer

watch:
	./scripts/watch
