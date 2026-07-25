# 实体配置指南

本指南用于说明如何通过 JSON 配置文件自定义 `潜行与警戒` 模组中各个实体的侦测、刺杀和警戒条件。

---

## 目录结构

### 1. 玩家配置路径
在游戏运行目录下，配置文件的存放位置如下：
> `config/stealth_and_alert/entities/<命名空间>/<实体ID>.json`

* **示例**：
  * 修改原版僵尸：`config/stealth_and_alert/entities/minecraft/zombie.json`
  * 修改其他模组实体（如暮色森林的米诺陶）：`config/stealth_and_alert/entities/twilightforest/minotaur.json`

### 2. 模组开发者预设路径
如果你是其他模组的开发者，希望为自己的生物提供默认的警戒配置，只需将 JSON 文件打包进你的模组 JAR 包：
> `data/stealth_and_alert/presets/entities/<命名空间>/<实体ID>.json`

*当游戏启动时，本模组会自动扫描所有模组的该目录，并释放未存在的配置文件到玩家的 `config` 文件夹中。*

**对于整合包作者来说**，你也可以将预设的 JSON 文件放到本模组 JAR 包中对应目录下。

---

## 完整 JSON 结构示例

以下是一个包含所有可配置项的完整 JSON 模板：

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
> 在 `detection` 对象中，若将数值设为 `-1` 或直接省略该字段，系统会自动继承 `stealth_and_alert-common.toml` 中的全局通用配置；`ignore_baby` 默认为 `false`。

| 字段名 | 数据类型    | 说明 |
| :--- |:--------| :--- |
| **`detection`** | Object  | **控制侦测机制的主节点** |
| `detection.ignore_baby` | Boolean | 该生物的幼崽是否能被触发警戒 |
| `detection.detection_range` | Double  | 视野最远距离 |
| `detection.horizontal_fov` | Double  | 水平 FOV |
| `detection.vertical_up_fov` | Double  | 向上 FOV |
| `detection.vertical_down_fov` | Double  | 向下 FOV |
| `detection.reaction_ticks` | Int     | 初次看到玩家时的反应时长 |
| `detection.tracking_ticks` | Int     | 看不到玩家时的追踪时长 |
| `detection.patience_ticks` | Int     | 对 LKP 感兴趣的耐心时长 |
| `detection.memory_ticks` | Int     | 被玩家激怒后对玩家的记忆时长 |
| **`assassination`** | Object  | **控制刺杀机制的主节点** |
| `assassination.success_chance` | Double  | 刺杀成功率，仅当全局配置中的刺杀必定成功被关闭后才起作用 |
| **`alert_conditions`** | Object  | **控制警戒条件的主节点，仅对 `conditional_seekers` 标签生物有效，其中所有项均可省略** |


`alert_conditions` 可用来控制中立生物对玩家的仇恨产生条件，即警戒条件。当然，普通敌对生物也可以应用这些条件。

> [!IMPORTANT]
> `alert_conditions` 中的条件**仅对拥有 `#stealth_and_alert:conditional_seekers` 实体标签**的生物生效。未拥有该标签的生物将直接忽略此节点。

> [!NOTE]
> 所有警戒条件均为“有即生效，没有即不生效”。
> 
> 另一个前提：生物必须看到玩家。

---

### 1. 结构与基本语法

`alert_conditions` 内部采用双层 Map 结构：**外层为条件类型，内层为具体的参数配置**。

```json
"alert_conditions": {
  "<命名空间:警戒条件_1>": {
    "range": 32.0,
    "items": ["minecraft:apple", "stealth_and_alert:shadow_crystal"],
    "invert": false
  },
  "<命名空间:警戒条件_2>": { ... }
}
```

### 2. 具体警戒条件配置
> [!IMPORTANT]
> 每个警戒条件的 ID 均应包含命名空间，在本模组下，命名空间一般只有本模组的 ID。例如：`stealth_and_alert:fight_back`。

> `stealth_and_alert:fight_back`
- 拥有此条件的生物，在被玩家攻击后会对玩家产生警戒。
- 对于大部分中立生物来说，一般是必选项。
- 该条件目前没有任何参数，但是在 JSON 中仍然要写成 `"stealth_and_alert:fight_back": {}`

> `stealth_and_alert:protect_others`
- 拥有此条件的生物，在指定的其他生物被玩家攻击后，会对玩家产生警戒。
- 典型代表：猪灵、狼、铁傀儡。
- ```json
  "stealth_and_alert:protect_others": {
    "entities": [
      "minecraft:piglin",
      "#minecraft:skeletons"
    ]
  }
  ```
- `entities` 是指定受保护的生物的列表，可以填入任意数量的实体 ID 或实体标签，必须包含命名空间；实体标签的最前面必须加上 `#` 号。
- 如果希望一种生物拥有类似猪灵和狼的群体愤怒机制，列表中必须填入该生物自己的 ID——本条件并不默认生物保护自己的种群。
> [!IMPORTANT]
> 受到保护的生物必须属于 `#stealth_and_alert:seekers` 或 `#stealth_and_alert:protected` 实体标签。
> 后者用于不会产生警戒的生物，比如：村民。

> `stealth_and_alert:light_sensitive`
- 拥有此条件的生物，对玩家产生警戒与否取决于光照强度。
- 典型代表：蜘蛛、洞穴蜘蛛。
- ```json
  "stealth_and_alert:light_sensitive": {
    "threshold": 11,
    "invert": false
  }
  ```
- `threshold` (`Int`) 指定了光照强度阈值。默认情况下，当光照强度小于或等于这个阈值时，生物即对玩家产生警戒。
- `invert` 指定了本条件判定逻辑是否反转，不填时默认为 `false`。当为 `true` 时，光照条件反转。

> `stealth_and_alert:eye_contact`
- 拥有此条件的生物，在被玩家注视时，会对玩家产生警戒。
- 典型代表：末影人。
- ```json
  "stealth_and_alert:eye_contact": {
    "pumpkin_mask": true
  }
  ```
- `pumpkin_mask` 指定了玩家戴上南瓜头后注视该生物时能否被无视。不填时默认为 `false`。

> `stealth_and_alert:close_to_child`
- 拥有此条件的生物，在玩家靠近其幼崽时，会对玩家产生警戒。
- 典型代表：北极熊。
- ```json
  "stealth_and_alert:close_to_child": {
    "horizontal_range": 8.0,
    "vertical_range": 4.0
  }
  ```
- `horizontal_range` 指定了玩家与幼崽距离的水平方向阈值。不填时默认为 `8.0`。
- `vertical_range` 指定了玩家与幼崽距离的垂直方向阈值。不填时默认为 `4.0`。
- 当玩家与幼崽之间的距离近到同时突破这两个阈值时，生物会对玩家产生警戒。

> `stealth_and_alert:item_wearing`
- 拥有此条件的生物，在玩家没有穿上指定的物品时，会对玩家产生警戒。
- 典型代表：猪灵。
- ```json
  "stealth_and_alert:item_wearing": {
    "items": [
      "minecraft:golden_helmet",
      "#minecraft:piglin_loved"
    ]
  }
  ```
- `items` 是指定穿上后不会被产生警戒的物品的列表，可以填入任意数量的物品 ID 或物品标签，必须包含命名空间；物品标签的最前面必须加上 `#` 号。
- 该条件将来可能会被改写，以实现更复杂的机制。

> `stealth_and_alert:action_on_container`
- 拥有此条件的生物，在玩家打开或破坏附近的容器时，会对玩家产生警戒。
- 典型代表：猪灵。
- ```json
  "stealth_and_alert:action_on_container": {
    "range": 16.0
  }
  ```
- `range` 指定了一个距离，在以生物为中心的这个距离内，玩家对容器的打开或破坏操作会使生物产生警戒。不填时，默认值为生物的视野最远距离 `detection.detection_range`。

> `stealth_and_alert:village_reputation`
- 拥有此条件的生物，在附近的村民对玩家声望足够低时，会对玩家产生警戒。
- 典型代表：铁傀儡。
- ```json
  "stealth_and_alert:village_reputation": {
    "threshold": -15,
    "range": 32.0
  }
  ```
- `threshold` (`Int`) 指定了一个阈值，当附近村民对玩家的声望小于或等于这个阈值时，生物即对玩家产生警戒。不填时默认值为 `-15`。
- `range` 指定了距离该生物多近范围内的村民的声望可以被检测。不填时默认值为 `16.0`。
