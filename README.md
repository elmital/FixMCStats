# FixMCStats

Fabric mod that aims to fix some issues with the Minecraft statistics.🤓

**The client side part only fixes issues with the statistics screen. You can use it on every server or in solo.
The server side part/fixes needs the mod to be installed on server side to work or that you play in solo.**

## Client side bug fixes

- [MC-80827](https://bugs.mojang.com/browse/MC-80827) : _Mob statistics have missing space between mob name and description_
- [MC-139386](https://bugs.mojang.com/browse/MC-139386) : _Rare blocks and items highlighted in the Statistics page do not show rarity colours_
- [MC-189484](https://bugs.mojang.com/browse/MC-189484) : _Statistics screen Tab selection is not centered_
- [MC-213103](https://bugs.mojang.com/browse/MC-213103) : _Item statistics are sorted arbitrarily by default_
- [MC-213104](https://bugs.mojang.com/browse/MC-213104) : _When resizing the Minecraft window while in the statistics screen, the tab resets to "General"_

## Server side bug fixes (This should works in Solo too)

- [MC-29519](https://bugs.mojang.com/browse/MC-29519) : _Damage dealt stat doesn't increase when using projectiles to attack entities_
- [MC-65198](https://bugs.mojang.com/browse/MC-65198) : _Craft statistics doesn't increase correctly when crafting items with shift-click or drop actions in stonecutter, crafting table and smithing table_
- [MC-111435](https://bugs.mojang.com/browse/MC-111435) : _Damage dealt stat doesn't increase when the sweeping attack damages nearby entities_
- [MC-122656](https://bugs.mojang.com/browse/MC-122656) : _Breaking elytra and trident never increase score_
- [MC-128079](https://bugs.mojang.com/browse/MC-128079) : _Statistic for using shears doesn't increase when mining certain blocks_
- [MC-144005](https://bugs.mojang.com/browse/MC-144005) : _Crafting stat for cooked items doesn't increase using campfires_
- [MC-147347](https://bugs.mojang.com/browse/MC-147347) : _Lighting a creeper with a flint and steel doesn't count as mob killed in statistics_
- [MC-148457](https://bugs.mojang.com/browse/MC-148457) : _Crawling increments the "Distance Walked" statistic_ *
- [MC-182814](https://bugs.mojang.com/browse/MC-182814) : _Drinking honey bottles increases used stat by two and runs the consume advancement trigger twice_
- [MC-154487](https://bugs.mojang.com/browse/MC-154487) : _Crafting potion stat doesn't increase using brewing stands_
- [MC-157098](https://bugs.mojang.com/browse/MC-157098) : _Statistics crafting counter fails to increment on partially full inventory_
- [MC-176806](https://bugs.mojang.com/browse/MC-176806) : _Stat for using glowstone doesn't increase score when charging a respawn anchor_
- [MC-211938](https://bugs.mojang.com/browse/MC-211938) : _Climbing scaffolding increases the jump stat every block climbed_
- [MC-214457](https://bugs.mojang.com/browse/MC-214457) : _You don't get the "Ol' Betsy" advancement if the crossbow breaks_
- [MC-231743](https://bugs.mojang.com/browse/MC-231743) : _Pottable plant used stat doesn't increase when placing plants into flower pots_
- [MC-254512](https://bugs.mojang.com/browse/MC-254512) : _Breaking a crossbow increases used air statistic_
- [MC-256638](https://bugs.mojang.com/browse/MC-256638) : _Riding a camel increments the 'Distance by Horse' statistic_ *
- [MC-259687](https://bugs.mojang.com/browse/MC-259687) : _"Distance by Elytra" statistic is approximately doubled_ **
- [MC-264274](https://bugs.mojang.com/browse/MC-264274) : _Lily pad and frogspawn do not increment "used" statistic when placed on water_

```
* the fix add a custom statistic and can be deactivated in the mod config file
** experimental fix, may not reflect reality 100% and can be deactivated in the mod config file
```

**Custom statistics created by the mod may not be translated in your language actually only French and English are supported.**

## Already fixed by Mojang / Won't fix

- [MC-80827](https://bugs.mojang.com/browse/MC-80827) : The "Statistic hover text is behind "Statistic" title (see screenshot)" is already fixed by Mojang the other part of issue is fix by the mod
- [MC-101240](https://bugs.mojang.com/browse/MC-101240) : Seems to be intended and more a translation issue
- [MC-201565](https://bugs.mojang.com/browse/MC-201565) : Seems to be intended, the other stat linked to sleeping is also set with the same context. Maybe a way to handle the multiplayer.
- [MC-204108](https://bugs.mojang.com/browse/MC-204108) : Another stat with a more generic name exist for cauldron when filling them so seems more a translation issue
- [MC-231909](https://bugs.mojang.com/browse/MC-231909) : Neither other furnaces work like that
- [MC-259673](https://bugs.mojang.com/browse/MC-259673) : Not only linked to the Stats
