# Mini's Mapping Viewer

This is a viewer for the official mappings for minecraft.
By the use of modern technology, this is way more performance than opening the mapping files in your browser or in an editor.

It also lets you compare mojang to spigot mappings and reverse (see the screenshots)

You can check it out here: https://minidigger.github.io/MiniMappingViewer

![https://i.imgur.com/fs1RIMm.png](https://i.imgur.com/fs1RIMm.png)
![https://i.imgur.com/zvntgtA.png](https://i.imgur.com/zvntgtA.png)

In the future, this will hopefully support more sets of mappings (yarn, mcp etc)  
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
