# Mini's Mapping Viewer

This is a viewer for the official mappings for minecraft.
By the use of modern technology, this is way more performance than opening the mapping files in your browser or in an editor.

You can check it out here: https://minidigger.github.io/MiniMappingViewer

In the future, this will allow you to easily compare different set of mappings (mojang, spigot, yarn, mcp etc)

![https://i.imgur.com/akVjy3x.png](https://i.imgur.com/akVjy3x.png)

Come back later to see how this is evolving.

Note: this webapp makes use of corsanywhere, hosted on a vps, to proxy requests to github, mojang and spigots stash if needed to prevent CORS errors.

## Dev Stuff

This project uses yarn and quasar. The most common commands are noted below

### Install the dependencies
```bash
yarn
```

#### Start the app in development mode (hot-code reloading, error reporting, etc.)
```bash
quasar dev
```

#### Lint the files
```bash
yarn run lint
```

#### Build the app for production
```bash
quasar build
```

## Licence

MIT
