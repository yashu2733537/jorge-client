# JORGE CLIENT v2.0 — Minecraft 26.3 Fabric

All-in-one hack client for the Wilderness Bound drop. Press **RIGHT SHIFT** to open the ClickGUI.

## Target
- Minecraft **26.3** (protocol 777), Mojang official mappings (26.1+ is deobfuscated — no Yarn)
- Fabric Loader **0.19.5**, Fabric API **0.161.0+26.3**, Loom **1.17**, Gradle **9.6.0**, **Java 25**

## Features (44 modules)

**Combat (9):** KillAura (Distance/Health/Angle, Snap rotations, APS), AutoClicker (CPS random + Crits),
Reach (3-6 blocks, `MultiPlayerGameMode#getPickRange` mixin), Velocity (Cancel/Reduce/JumpReset, `Entity#push` mixin),
Criticals (Packet/Jump/MiniJump via `ServerboundMovePlayerPacket$Pos`), AutoBlock, HitBoxes, WTap, AntiBot
**Movement (12):** Sprint, Speed (Strafe/Hop/LowHop/MatrixHop), Fly (Creative/Vanilla/Jetpack),
LongJump, Step (`STEP_HEIGHT` attribute), NoSlow, Scaffold + Tower, Spider, Jesus, NoFall (Packet/Spoof),
Phase, SafeWalk
**Player (9):** AutoEat, AutoArmor, ChestStealer, InvManager, AutoTool, FastBreak (`destroyDelay` mixin),
Blink (packet choke), NoRotate, AutoRespawn (respawn packet)
**Render (8):** ESP (Box/Glow + 3D line boxes), Tracers (3D lines), Nametags, FullBright (Night Vision),
HUD (Watermark/ArrayList/Coords/FPS via `HudElementRegistry`), StorageESP, ItemESP, Chams
**World (4):** Nuker, AutoMine, FastPlace (`rightClickDelay` mixin + tick fallback), Timer
**Exploit (2):** Disabler (Matrix/Vulcan), PacketFly

## ClickGUI (RSHIFT)
- Draggable category panels, left-click toggle, right-click expands settings
- Boolean / Mode (click to cycle) / Number (click + drag slider)

## Build (needs JDK 25)
```bash
./gradlew build
# jar: build/libs/jorge-client-2.0.0.jar -> put in mods/ with Fabric API
```

## Notes vs 1.20.1 build
- `HudRenderCallback` was removed by Fabric — HUD now uses `HudElementRegistry.attachElementBefore(CHAT, ...)`
  with `GuiGraphicsExtractor` (`fill` / `text` / `centeredText`).
- `WorldRenderEvents` is now `LevelRenderEvents.AFTER_ENTITIES` (`poseStack` / `bufferSource` / `camera`).
- Mixins are `required: false` / `defaultRequire: 0`, so if a Mojang name drifts in a 26.3 point release
  the client still starts (that module's mixin part just stays inactive).
- Run `./gradlew genSources` in the IDE and fix any name drift the compiler reports.
