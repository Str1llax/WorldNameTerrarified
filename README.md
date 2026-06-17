# World Name Terrarified - WNT
## About
WNT is a small Minecraft mod, which adds only one (for now) simple feature. Random world name generator exactly like in Terraria! (Not 100% sure, but anyway)
## Downloads
- [Modrinth](https://modrinth.com/mod/world-name-terrarified)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/world-name-terrarified)


***And if you post this mod on a third-party websites, please include a link to the official download from the platforms listed above. Thank you <3***
## How it works
Mod adds a small square button with letter 'R' on it to the world creation screen, on the left from world name field. When you press the button it chooses 4 random entries from specific files and creates a *unique* name.
## Compatibilities
Right now it officially supports 4 languages:
- [x] English - en_us (**1 887 189 984** *unique* world names)
- [x] Russian - ru_ru (**944 663 616** *unique* world names)
- [x] Portuguese - pt_pt (**943 594 992** *unique* world names)
- [x] Simplified Chinese - zh_cn (**943 594 992** *unique* world names)
- [ ] ... more comming... maybe...
- [x] *Infinite* expansion with custom resourcepacks!
## Customization
Mod uses Minecraft's resource manager (simpler Resourcepacks) to access files, so it's very simple to add your own variants/localizations!
### Creating custom resourcepack
First of all you need to know how to handle resources, here is the structure of 'resourcepack':
```
assets/
├─ wnt/
│  ├─ LOCALE_CODE/
│  │  ├─ adjectives.txt
│  │  ├─ compositions.txt
│  │  ├─ locations.txt
│  │  ├─ nouns.txt
```
Important things to know about it:
- `LOCALE_CODE` folder (en_us, ru_ru, etc.) - **MUST** be *lowercase*. If you don't know language code you can simply google it or if locale is not supported by the mod you can found its code in the logs under 'Error while loading localization files...'
- compositions.txt is the template of world names, here is its structure([example](https://github.com/Str1llax/WorldNameTerrarified/blob/master/src/main/resources/assets/wnt/en_us/compositions.txt)):
  - `@` - Adjective
  - `#` - Location
  - `$` - Noun
- `adjectives.txt, locations.txt, nouns.txt` - are just lists with variants separated by lines, they'll replace `@`, `#` or `$`([examples](https://github.com/Str1llax/WorldNameTerrarified/tree/master/src/main/resources/assets/wnt/en_us))
- Names of the files **MUST** be exact the same as shown above!\
\
So to modify the original mod you simply need to create a resource pack with a suitable pack.mcmeta and directory `assets/wnt/LOCALE_CODE/` where you'll put the files you'd like to modify or add. You can also override already existing resources as well as adding new locales!
## Credits
Thanks!\
[terraria.wiki.gg](https://terraria.wiki.gg/wiki/World/Name) for provided lists of words which this mod uses.
