[简体中文](ENTITY_CONFIG.zh-CN.md)

# Entity Configuration Guide

This guide is intended to show how to custom entities' settings of detection, assassination and alert conditions
through JSON files in `Stealth and Alert`.

---

## File Structure

### 1. User Config
In game's run directory, the JSON configs are saved in
> `config/stealth_and_alert/entities/<namespace>/<Entity ID>.json`

* **e.g.**：
  * Modify vanilla zombie: `config/stealth_and_alert/entities/minecraft/zombie.json`
  * Modify mobs of other mods (e.g., Minotaur of Twilight Forest): `config/stealth_and_alert/entities/twilightforest/minotaur.json`

### 2. Mod Developer Presets
If you are a mod developer who wishes to provide your mobs with default alert configs, put JSONS into JAR of your mod.
> `data/stealth_and_alert/presets/entities/<namespace>/<Entity ID>.json`

*As the game launches, the mod scans such directory of all mods, 
and copy files not existing in `config` directory to players' `config` directory.*

**To modpack creators**, you can also put your JSON presets into my mod's JAR's corresponding directory.

---

## Full Template

Here's a full JSON template including all configurable settings.

```json
{
  "detection": {
    "ignore_baby": false,
    "detection_range": 16.0,
    "horizontal_fov": 120.0,
    "vertical_up_fov": 45.0,
    "vertical_down_fov": 45.0,
    "reaction_ticks": 10,
    "tracking_ticks": 30,
    "patience_ticks": 100,
    "memory_ticks": 300
  },
  "assassination": {
    "success_chance": 0.75
  },
  "alert_conditions": {}
}
```

> [!TIP]
> In `detection`, if a field is set to `-1` or is omitted, it will use the global universal value defined in `stealth_and_alert-common.toml`; 
> `ignore_baby` is `false` by default.

| Field                          | Data Type | Description                                                                                                                  |
|:-------------------------------|:----------|:-----------------------------------------------------------------------------------------------------------------------------|
| **`detection`**                | Object    | **Main node controlling detection mechanisms**                                                                               |
| `detection.ignore_baby`        | Boolean   | Whether this mob's babies' alert can be aroused                                                                              |
| `detection.detection_range`    | Double    | Max detection range within which mob can see players                                                                         |
| `detection.horizontal_fov`     | Double    | Horizontal FOV of mob's vision                                                                                               |
| `detection.vertical_up_fov`    | Double    | Upward FOV of mob's vision                                                                                                   |
| `detection.vertical_down_fov`  | Double    | Downward FOV of mob's vision                                                                                                 |
| `detection.reaction_ticks`     | Int       | The reaction time required for mob to fully perceive a player after spotting them                                            |
| `detection.tracking_ticks`     | Int       | The duration before mob loses track of a player since unable to see them                                                     |
| `detection.patience_ticks`     | Int       | The duration of patience before mob loses interest in an LKP                                                                 |
| `detection.memory_ticks`       | Int       | The duration of mob's memory towards a player who has enraged it                                                             |
| **`assassination`**            | Object    | **Main node controlling assassination mechanisms**                                                                           |
| `assassination.success_chance` | Double    | The chance of successfully performing an assassination towards mob, only valid when Assassination Always Success is false    |
| **`alert_conditions`**         | Object    | **Main node controlling alert conditions, only valid to mobs in `conditional_seekers` tag. All sections can be not written** |


`alert_conditions` can be used to control hatred conditions of neutral mob to a player. Of course, they can be applied to normal hostile mob, too.

> [!IMPORTANT]
> `alert_conditions` is only valid to mobs in **`#stealth_and_alert:conditional_seekers` entity tag**, ignored by mobs not in it.

> [!NOTE]
> All alert conditions is 'valid if present; void if not'.
> 
> Another precondition: mobs must see players.

---

### 1. Syntax Structure

`alert_conditions` internally uses a two-layer Map structure: **the outer layer represents the condition type, and the inner layer holds the specific parameter configurations**。

```json
"alert_conditions": {
  "<namespace:condition_1>": {
    "range": 32.0,
    "items": ["minecraft:apple", "stealth_and_alert:shadow_crystal"],
    "invert": false
  },
  "<namespace:condition_2>": { ... }
}
```

### 2. Detailed Alert Conditions Configs
> [!IMPORTANT]
> Every alert condition's ID should include namespace, here namespace is only this mod's ID. e.g., `stealth_and_alert:fight_back`.

> `stealth_and_alert:fight_back`
- Mobs with this condition will become alert towards a player after being attacked by the player.
- For most neutral mobs, this is generally a mandatory option.
- This condition currently has no parameters, but must still be written in JSON as `"stealth_and_alert:fight_back": {}`

> `stealth_and_alert:protect_others`
- Mobs with this condition will become alert towards a player when a specified other mob is attacked by the player.
- Typical examples: Piglin, Wolf, Iron Golem.
- ```json
  "stealth_and_alert:protect_others": {
    "entities": [
      "minecraft:piglin",
      "#minecraft:skeletons"
    ]
  }
  ```
- `entities` is a list of protected entities. You can fill in any number of mob IDs or entity tags, and they must include a namespace. Entity tags must be prefixed with `#`.
- If you want an entity to have a pack-anger mechanic similar to Piglins or Wolves, you must include its own ID in the list — this condition does not default to protecting its own species.
> [!IMPORTANT]
> The protected entities must belong to either the `#stealth_and_alert:seekers` or `#stealth_and_alert:protected` entity tag.
> The latter is used for entities that do not generate alert on their own, such as Villagers.

> `stealth_and_alert:light_sensitive`
- Mobs with this condition decide whether to become alert towards a player based on light level.
- Typical examples: Spider, Cave Spider.
- ```json
  "stealth_and_alert:light_sensitive": {
    "threshold": 11,
    "invert": false
  }
  ```
- `threshold` (`Int`) specifies the light level threshold. By default, the entity becomes alert towards the player when the light level is less than or equal to this value.
- `invert` specifies whether to invert the logic of this condition. Defaults to `false` when omitted. When `true`, the light condition is reversed.

> `stealth_and_alert:eye_contact`
- Mobs with this condition become alert towards a player when looked at.
- Typical example: Enderman.
- ```json
  "stealth_and_alert:eye_contact": {
    "pumpkin_mask": true
  }
  ```
- `pumpkin_mask` specifies whether the entity can be safely looked at when the player wears a carved pumpkin. Defaults to `false` when omitted.

> `stealth_and_alert:close_to_child`
- Mobs with this condition become alert towards a player when the player gets close to their babies.
- Typical example: Polar Bear.
- ```json
  "stealth_and_alert:close_to_child": {
    "horizontal_range": 8.0,
    "vertical_range": 4.0
  }
  ```
- `horizontal_range` specifies the horizontal distance threshold between the player and the baby. Defaults to `8.0` when omitted.
- `vertical_range` specifies the vertical distance threshold between the player and the baby. Defaults to `4.0` when omitted.
- When the player's distance to the baby breaches both thresholds simultaneously, the entity becomes alert towards the player.

> `stealth_and_alert:item_wearing`
- Mobs with this condition become alert towards a player when the player is not wearing at least one specified item.
- Typical example: Piglin.
- ```json
  "stealth_and_alert:item_wearing": {
    "items": [
      "minecraft:golden_helmet",
      "#minecraft:piglin_loved"
    ]
  }
  ```
- `items` is a list of items that prevent alert when worn. Any number of item IDs or item tags can be entered, and must include a namespace. Item tags must be prefixed with `#`.
- This condition may be rewritten in the future to support more complex mechanics.

> `stealth_and_alert:action_on_container`
- Mobs with this condition become alert towards a player when the player opens or destroys a nearby container.
- Typical example: Piglin.
- ```json
  "stealth_and_alert:action_on_container": {
    "range": 16.0
  }
  ```
- `range` specifies a distance. Within this distance centered on the entity, the player opening or destroying a container will make the entity alert. When omitted, defaults to the entity's maximum vision distance (`detection.detection_range`).

> `stealth_and_alert:village_reputation`
- Mobs with this condition become alert towards a player when the reputation of nearby villagers with the player is low enough.
- Typical example: Iron Golem.
- ```json
  "stealth_and_alert:village_reputation": {
    "threshold": -15,
    "range": 32.0
  }
  ```
- `threshold` (`Int`) specifies a threshold. When the reputation of nearby villagers with a player is less than or equal to this value, the entity becomes alert towards the player. Defaults to `-15` when omitted.
- `range` specifies the radius within which villagers' reputations are checked. Defaults to `16.0` when omitted.
