# Chat app sử dụng giao thức tự thiết kế

- Mới vào: client gửi LOGIN hoặc REGISTER:

### Login
- Client gửi `"LOGIN\n" + "username\n" + "password\n"`
- Server trả về `"LOGIN SUCESSFUL\n"` hoặc `"LOGIN FAILED\n"`  

### Register
- username không được có dấu `":"` hoặc `"\n"`
- Client gửi `"REGISTER\n" + "username\n" + "password\n"`  
- Server trả về `"REGISTER SUCCESSFUL\n"` hoặc `"REGISTER FAILED\n"`  

### Show friend list
- Login/register xong -> show danh sách bạn bè online đầu offline sau  
- Client gửi `"SHOW FRIEND\n"`
- Server gửi `"FRIEND STATUS\n" + a + "\n"` với `a` là số lượng bạn + `a` dòng kết quả `friend_name: online\n` hoặc `friend_name: offline\n`

### Add friend  
- Client gửi `"ADD FRIEND\n" + "username\n"`, username của người muốn add
- Server gửi `"FRIEND REQUEST\n" + "username\n"`, username của người gửi friend request  
- Client của người được gửi friend request gửi `"FRIEND REQUEST ACCEPTED\n"` hoặc `"FRIEND REQUEST REJECTED\n"` đến server

### Choose friend to chat  
- Client1 gửi `"CHAT\n" + "username\n" + "port\n"`
- Server tra nếu người này offline gửi lại `"CHAT NOT AVAILABLE\n"` nếu người này online gửi tới người muốn connect `"CHAT REQUEST\n" + "username\n" + "port\n`, username của người gửi chat request
- Client2 của người được gửi friend request gửi `"CHAT REQUEST ACCEPTED\n"` hoặc `"CHAT REQUEST REJECTED\n"` đến server 
- Server nếu nhận `"CHAT REQUEST REJECTED\n"` sẽ gửi cho Client1 `"CHAT REJECTED\n"`, nếu nhận `"CHAT REQUEST ACCEPTED\n"` sẽ gửi cho cả Client1 và Client2 `CHAT INFO\n ip:port:server\n` với ip chỉ làm IPv4, `server` là `0` hoặc `1` chỉ định client đó sẽ là đóng vai trò server trong kết p2p

### Kết nối P2P
- Gửi tin nhắn `"MESSAGE\n" + "len_of_message\n" + "msg"`. Ví dụ gửi tin nhắn "AAAA" -> `"MESSAGE\n" + "4\n" + "AAAA"`
- Gửi file `"FILE\n" + "name_of_file\n" + "len_of_file\n" + "data_in_file"`

