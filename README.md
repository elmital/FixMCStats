# <u>FixMCStats</u>
Fabric mod that aims to fix some issues with the Minecraft statistics.🤓

This is an unofficial NeoForge Port and I have limited scope for testing, please report it to me if something goes wrong.

If you are the original author of the mod, you can freely extract that Fork back into your project, I don't ask for anything.

---
## <u>Fixes</u>

**The client side part only fixes issues with the statistics screen. You can use the mod on every server or in solo and this part will work.
The server side part/fixes needs the mod to be installed on server side to work or that you play in solo.**

### Client side bug fixes :

- [MC-80827](https://bugs.mojang.com/browse/MC-80827) : _Mob statistics have missing space between mob name and description_
- [MC-139386](https://bugs.mojang.com/browse/MC-139386) : _Rare blocks and items highlighted in the Statistics page do not show rarity colours_
- [MC-178516](https://bugs.mojang.com/browse/MC-178516) : _Statistics are not sorted correctly in languages using non-ASCII letters_** (v1.3.0+)
- [MC-213103](https://bugs.mojang.com/browse/MC-213103) : _Item statistics are sorted arbitrarily by default_

### Server side bug fixes (This works in Solo too) :

- [MC-36696](https://bugs.mojang.com/browse/MC-36696) : _Clicking on the statistics button on the menu screen advances the game by 1 tick_* (v1.3.2+) only solo mode is affected
- [MC-29519](https://bugs.mojang.com/browse/MC-29519) : _Damage dealt stat doesn't increase when using projectiles to attack entities_
- [MC-65198](https://bugs.mojang.com/browse/MC-65198) : _Craft statistics doesn't increase correctly when crafting items with shift-click or drop actions in stonecutter, crafting table and smithing table_
- [MC-111435](https://bugs.mojang.com/browse/MC-111435) : _Damage dealt stat doesn't increase when the sweeping attack damages nearby entities_
- [MC-122656](https://bugs.mojang.com/browse/MC-122656) : _Breaking elytra and trident never increase score_
- [MC-121541](https://bugs.mojang.com/browse/MC-121541) : _Scoreboard objective criteria for distance fallen doesn't track fall distance when player lands in liquid, powder snow, or cobweb_ (v1.3.0+) ***
- [MC-144005](https://bugs.mojang.com/browse/MC-144005) : _Crafting stat for cooked items doesn't increase using campfires_
- [MC-147347](https://bugs.mojang.com/browse/MC-147347) : _Lighting a creeper with a flint and steel doesn't count as mob killed in statistics_
- [MC-148457](https://bugs.mojang.com/browse/MC-148457) : _Crawling increments the "Distance Walked" statistic_ 
- [MC-154487](https://bugs.mojang.com/browse/MC-154487) : _Crafting potion stat doesn't increase using brewing stands_
- [MC-176806](https://bugs.mojang.com/browse/MC-176806) : _Stat for using glowstone doesn't increase score when charging a respawn anchor_
- [MC-211938](https://bugs.mojang.com/browse/MC-211938) : _Climbing scaffolding increases the jump stat every block climbed_
- [MC-214457](https://bugs.mojang.com/browse/MC-214457) : _You don't get the "Ol' Betsy" advancement if the crossbow breaks_
- [MC-231743](https://bugs.mojang.com/browse/MC-231743) : _Pottable plant used stat doesn't increase when placing plants into flower pots_
- [MC-245962](https://bugs.mojang.com/browse/MC-245962) : _Times mined statistic displays wrong value for some blocks_ (v1.3.0+)
- [MC-254512](https://bugs.mojang.com/browse/MC-254512) : _Breaking a crossbow increases used air statistic_
- [MC-256638](https://bugs.mojang.com/browse/MC-256638) : _Riding a camel increments the 'Distance by Horse' statistic_ 
- [MC-264274](https://bugs.mojang.com/browse/MC-264274) : _Lily pad and frogspawn do not increment "used" statistic when placed on water_
- [MC-265376](https://bugs.mojang.com/browse/MC-265376) : _Kills by Goats are not counted in statistics_ (v1.1.0+)
- [MC-268093](https://bugs.mojang.com/browse/MC-268093) : _Breaking a decorated pot with an arrow doesn't affect statistics_ (v1.1.0+)

```
* the fix add a custom statistic and can be deactivated in the mod config file
** the fix use your computer configured language (or the JVM one if using arg) simply change the translation in MC will not fix the bug
*** the issue had been fixed partially by Mojang since 1.21.5 but still exists for some blocks
```

**Custom statistics created by the mod may not be translated in your language actually only French(ca, fr), English(gb, us, ca, au), German, Polish, Russian, Chinese Simplified, Arabic, Spanish(ar,es,mx) and Portuguese(pt,br) are supported.**

### Already fixed by Mojang / No more fixed by the mod :

- [MC-80827](https://bugs.mojang.com/browse/MC-80827) : The "Statistic hover text is behind "Statistic" title (see screenshot)" is already fixed by Mojang the other part of issue is fixed by the mod
- [MC-128079](https://bugs.mojang.com/browse/MC-128079) : _Statistic for using shears doesn't increase when mining certain blocks_ Since MC 1.21.5 the bug has been fixed and is no more included in versions above 1.3.2 of the mod
- [MC-154487](https://bugs.mojang.com/browse/MC-154487) : _"Crafting potion stat doesn't increase using brewing stands"_ Since MC 1.21 the issue had been fixed partially for shift clicking by Mojang with the [MC-271199](https://bugs.mojang.com/browse/MC-271199) fix but issue still appears for other clicking and is fixed by the mod
- [MC-157098](https://bugs.mojang.com/browse/MC-157098) : _Statistics crafting counter fails to increment on partially full inventory_ Since MC 1.21 the bug has been fixed even if the issue is still opened and the patch is no more included in versions above 1.1.0 of the mod
- [MC-182814](https://bugs.mojang.com/browse/MC-182814) : _Drinking honey bottles increases used stat by two and runs the consume advancement trigger twice_ Since MC 1.21.2 with changes made by suppressing the HoneyBottleItem class Mojang indirectly fix it and the patch is no more included in versions above 1.1.1 of the mod 
- [MC-189484](https://bugs.mojang.com/browse/MC-189484) : _"Statistics screen Tab selection is not centered"_ Since MC 1.20.5 the bug has been fixed and is no more included in versions above 1.0.0 of the mod
- [MC-213104](https://bugs.mojang.com/browse/MC-213104) : _When resizing the Minecraft window while in the statistics screen, the tab resets to "General"_ Since MC 1.21.2 the bug has been fixed and is no more included in versions above 1.1.1 of the mod
- [MC-259687](https://bugs.mojang.com/browse/MC-259687) : _"Distance by Elytra" statistic is approximately doubled_ Since MC 1.21.2 the bug has been fixed and is no more included in versions above 1.1.1 of the mod
- [MC-267006](https://bugs.mojang.com/browse/MC-267006) : _The Distance Flown statistics rapidly increases when you are standing in Ender Dragon's hitbox_ Since MC 1.21.2 the bug has been fixed and is no more included in versions above 1.1.1 of the mod


### Won't fix :

- [MC-101240](https://bugs.mojang.com/browse/MC-101240) : Seems to be intended and more a translation issue
- [MC-201565](https://bugs.mojang.com/browse/MC-201565) : Seems to be intended, the other stat linked to sleeping is also set with the same context. Maybe a way to handle the multiplayer.
- [MC-204108](https://bugs.mojang.com/browse/MC-204108) : Another stat with a more generic name exist for cauldron when filling them so seems more a translation issue
- [MC-231909](https://bugs.mojang.com/browse/MC-231909) : Neither other furnaces work like that
- [MC-259673](https://bugs.mojang.com/browse/MC-259673) : Not only linked to the Stats

---
## <u>Issue tracker</u>

When you open a ticket either to report a bug or for an enhancement request make sure that no other ticket already exists. 
Always separate your tickets one ticket by bug/request.

### Report an issue (with the mod) :

- No warranty is provided about the full functionality of the mod however bugs reports especially good ones are appreciated.
- Try to use a title that briefly describe the problem.
- Describe the problem and how to reproduce it.
- Join logs, crash report, screenshots or all other things that can help to illustrate your report or reproducing steps.
- Don't report a Minecraft bug as a bug.

### Want a Minecraft bug to be fixed by the mod ?

- No guarantee is provided for a patch to be added by the mod. A MC bug report is always analyzed in depth to ensure that a possible fix is aligned with behaviors that I believe to be close to vanilla based on comments in the Mojang Mojira and my own opinion.
- The bug must be linked to MC statistics.
- Don't ask for a bug which is a translation issue. I can barely talk in english imagine for all other languages MC supports.
- Always refers to an official Minecraft bug from the [official issue tracker](https://bugs.mojang.com/projects/MC/issues).
- The bug should occur on MC release versions not only on snapshots for being eventually fixed.
- Make sure the bug doesn't appear in the **Won't fix** section already.

---

## Support

Want to support me and other creators by the same time? Disable your ad blocker while you are on Modrinth it's free and the website use ethical and non-invasive ads ;)
