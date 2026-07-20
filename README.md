# KnuaWhitelistBlock

[**English**](#english) | [**Tiếng Việt**](#tiếng-viet)

---

<a name="english"></a>

## English

A highly professional, modern, and lightweight Minecraft Paper/Folia plugin designed for administrators who want full control over block placement and breaking rules on a per-world basis.

### Features
* **Dual Platform Support**: Runs flawlessly on both traditional single-threaded servers (Spigot/Paper) and regional multi-threaded servers (Folia).
* **Per-World Rules**: Configure whitelist or blacklist modes for block break and place events independently for each world.
* **Modern Formatting**: Complete integration with Kyori's **MiniMessage** (supporting hex colors, gradients, and hover effects).
* **Dynamic Customizations**: Customize warnings per action including chat messages, action bars, and sound effects.
* **Robust & Idiot-Proof Configuration**:
  * Automatically filters out typos and invalid material names on startup with descriptive console warnings.
  * Try-catch parser protection prevents settings from being wiped or corrupted if a syntax error is made.
  * Automatic configuration upgrades preserve older values when upgrading versions.
* **Maximum Performance & Memory Optimizations**:
  * Block lists are parsed and queried directly using fast `Material` enums instead of raw string comparison.
  * Checks are dynamically ordered to skip expensive permission looks if a block is already allowed by rules.
  * Safe thread-local and asynchronous operations to ensure zero impact on game ticks.
* **Modular Codebase**: Clean OOP structure utilizing a clean SubCommand router pattern.

### Technical Details
* **Java Version**: Java 21 or higher
* **Minecraft Version**: 1.20 - 1.21+ (Paper, Purpur, Spigot, etc.)
* **Folia Support**: Fully Supported (fully compatible with regional multi-threading)

### "Is this AI slop?"
> [!NOTE]
> Yeah, it was written with the assistance of AI. It's 2026 and people are still boycotting AI LMAO this guy is so tuff.

---

### Commands & Permissions

All commands share the aliases `/whitelistblock` and `/kwb`.

| Command | Description | Permission | Default |
| :--- | :--- | :--- | :--- |
| `/kwb list [world/all]` | Displays allowed/blocked blocks in a world. | `knuawhitelistblock.admin` | `op` |
| `/kwb place <add/remove> [block] [world]` | Adds or removes a block from placing rules. | `knuawhitelistblock.admin` | `op` |
| `/kwb break <add/remove> [block] [world]` | Adds or removes a block from breaking rules. | `knuawhitelistblock.admin` | `op` |
| `/kwb reload` | Reloads plugin configurations and translation files. | `knuawhitelistblock.admin` | `op` |
| `/kwb help` | Shows the dynamic help menu. | `knuawhitelistblock.admin` | `op` |

*Note: For block place/break commands, if the block parameter is omitted, the block in the player's main hand is used. If the world parameter is omitted, the player's current world is used.*

#### Bypass Permissions
* `knuawhitelistblock.bypass`: Bypasses all placement and break restrictions.
* `knuawhitelistblock.bypass.place.<material>`: Bypasses placement checks for a specific block type (e.g., `knuawhitelistblock.bypass.place.tnt`).
* `knuawhitelistblock.bypass.break.<material>`: Bypasses breaking checks for a specific block type.

---

### Compilation

Build the plugin with Java 21 and Maven:
```bash
mvn clean package
```
The shaded jar will be located under the `target/` directory.

---

<a name="tiếng-viet"></a>

## Tiếng Việt

Một plugin Minecraft Paper/Folia chuyên nghiệp, hiện đại và siêu nhẹ giúp quản trị viên kiểm soát hoàn toàn quy tắc đặt (place) và đập (break) block ở từng thế giới khác nhau.

### Tính năng nổi bật
* **Hỗ trợ đa nền tảng**: Hoạt động mượt mà trên cả máy chủ truyền thống (Spigot/Paper) và máy chủ đa luồng khu vực (Folia).
* **Quy tắc riêng cho từng thế giới**: Cấu hình chế độ Whitelist hoặc Blacklist độc lập cho sự kiện đập và đặt block của từng thế giới.
* **Định dạng màu sắc hiện đại**: Tích hợp 100% **MiniMessage** (hỗ trợ màu Hex, dải màu Gradient, hiệu ứng hover/click).
* **Tùy biến đa dạng**: Tùy chỉnh thông điệp cảnh báo động bao gồm tin nhắn chat, thanh hành động (action bar), và âm thanh (sound).
* **Hệ thống cấu hình an toàn & tự sửa lỗi**:
  * Tự động lọc các tên block bị nhập sai chính tả khi khởi động, ghi cảnh báo lên Console và giữ plugin hoạt động bình thường thay vì crash.
  * Cơ chế Try-Catch ngăn chặn hoàn toàn việc xóa trắng hoặc làm hỏng file cấu hình khi admin viết sai cú pháp YAML.
  * Tự động cập nhật thêm các trường cấu hình mới khi nâng cấp phiên bản mà không làm mất cài đặt cũ.
* **Tối ưu hóa Hiệu năng & Bộ nhớ vượt bậc**:
  * Danh sách block được chuyển từ dạng chuỗi thô sang tập hợp kiểu `Material` enum để so sánh tốc độ cao trong bộ nhớ.
  * Thay đổi trật tự logic: Bỏ qua hoàn toàn việc kiểm tra quyền hạn (permission lookup) tốn CPU nếu block đặt/đập được quy tắc cho phép.
* **Mã nguồn sạch**: Cấu trúc thiết kế OOP rõ ràng, sử dụng mẫu phân phối SubCommand router chuyên nghiệp.

### Thông số kỹ thuật
* **Phiên bản Java**: Java 21 trở lên
* **Phiên bản Minecraft**: 1.20 - 1.21+ (Paper, Purpur, Spigot,...)
* **Hỗ trợ Folia**: Hoàn toàn tương thích (đã tối ưu hóa đa luồng khu vực)

### "Đây có phải là AI slop không?"
> [!NOTE]
> Ừ, nó được viết dưới sự hỗ trợ của AI đó. 2026 rồi còn bài trừ AI LMAO this guy is so tuff.

---

### Lệnh & Quyền hạn

Tất cả các lệnh đều hỗ trợ viết tắt qua `/whitelistblock` hoặc `/kwb`.

| Lệnh | Mô tả | Quyền hạn | Mặc định |
| :--- | :--- | :--- | :--- |
| `/kwb list [world/all]` | Xem danh sách block được phép/chặn ở thế giới. | `knuawhitelistblock.admin` | `op` |
| `/kwb place <add/remove> [block] [world]` | Thêm/bớt block ra khỏi danh sách quy tắc đặt. | `knuawhitelistblock.admin` | `op` |
| `/kwb break <add/remove> [block] [world]` | Thêm/bớt block ra khỏi danh sách quy tắc đập. | `knuawhitelistblock.admin` | `op` |
| `/kwb reload` | Tải lại cấu hình và các tệp tin ngôn ngữ dịch thuật. | `knuawhitelistblock.admin` | `op` |
| `/kwb help` | Hiển thị menu trợ giúp lệnh động. | `knuawhitelistblock.admin` | `op` |

*Lưu ý: Nếu tham số block để trống, plugin sẽ lấy block trên tay chính của người chơi. Nếu tham số world để trống, plugin sẽ lấy thế giới mà người chơi đang đứng.*

#### Quyền bypass (Bỏ qua kiểm tra)
* `knuawhitelistblock.bypass`: Bỏ qua tất cả các kiểm tra đặt và đập block.
* `knuawhitelistblock.bypass.place.<material>`: Bỏ qua kiểm tra đặt cho block cụ thể (ví dụ: `knuawhitelistblock.bypass.place.tnt`).
* `knuawhitelistblock.bypass.break.<material>`: Bỏ qua kiểm tra đập cho block cụ thể.

---

### Biên dịch dự án

Yêu cầu Java 21 và Maven:
```bash
mvn clean package
```
Tệp JAR thành phẩm nằm trong thư mục `target/`.
