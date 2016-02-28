using System;
using UnityEngine;
using SuperSocket.ClientEngine;
using System.Net;
using System.IO;
using Google.ProtocolBuffers;
using Google.ProtocolBuffers.Serialization;
using System.Net.Sockets;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.InteropServices;
using System.ComponentModel;
using System.Xml.Serialization;
using System.Xml;
using protoconf;

public class ProtoBufTest : MonoBehaviour {
	public string host 	= "127.0.0.1";
	public int port 	= 1024;

	public static DataEventArgs buffer = new DataEventArgs();
	public static int count = 0;

	void OnGUI() {

		if (GUI.Button (new Rect(100, 130, 100, 25), "SuperSocket")) {
			buffer.Data = new byte[8192];
			IPEndPoint endPoint = new IPEndPoint(IPAddress.Parse(host), port);

			AsyncTcpSession client = new AsyncTcpSession(endPoint);
			client.Connected 	+= OnConnected;
			client.DataReceived += OnDataReceive;

			client.Connect();

			NamePacket.Builder b = new NamePacket.Builder();
			b.SetName("Client name data");
			NamePacket np = b.BuildPartial();

			MemoryStream stream = new MemoryStream();
			np.WriteTo(stream);
			byte[] bytes = stream.ToArray();

			BasePacket.Builder basePacketBuilder = new BasePacket.Builder();
			basePacketBuilder.SetPacketId(64553060);
			basePacketBuilder.SetPacketData(
				ByteString.CopyFrom(bytes)
			);
			
			BasePacket basePacket = basePacketBuilder.BuildPartial();

			sendMessage(client, basePacket);

			print("SuperSocket!");
		}
	}
	
	public static void OnConnected(System.Object sender, EventArgs e) {
		print("connect to server finish.");
	}

	public static void sendMessage(AsyncTcpSession client, BasePacket packet) {
//		using (MemoryStream sendStream = new MemoryStream()) {
//			CodedOutputStream os = CodedOutputStream.CreateInstance(sendStream);			
// 			WriteMessageNoTag equivalent to WriteVarint32, WriteByte (byte []) 
// 			that is: variable length message header + message body 
//			os.WriteMessageNoTag(request);
//			os.WriteRawBytes(bytes);
//			os.Flush();

			MemoryStream sendStream = new MemoryStream();
			packet.WriteTo(sendStream);
			byte[] bytes = sendStream.ToArray();
			client.Send(new ArraySegment<byte>(bytes));
//		}
	}

	public static void OnDataReceive(System.Object sender, DataEventArgs e) {
		print("buff length: " + e.Length + ", offset: " + e.Offset);

		if (e.Length <= 0) return;
//		CodedInputStream stream = CodedInputStream.CreateInstance(e.Data);
//		int length = (int)stream.ReadRawVarint32();
//		byte[] baseBytes = stream.ReadRawBytes (e.Length);
//		byte[] baseBytes = stream.ToArray();

		MemoryStream stream = new MemoryStream(e.Data);
		byte[] baseBytes = new byte[e.Length];
		stream.Read(baseBytes, e.Offset, e.Length);

		BasePacket basePacket = BasePacket.ParseFrom(baseBytes);
		int packetId = basePacket.PacketId;

		byte[] namePacketBytes = basePacket.PacketData.ToByteArray();
		NamePacket response = NamePacket.ParseFrom(namePacketBytes); 

		print("packetId - " + packetId + ", response - " + response);
	}

	public static void OnDataReceive2(System.Object sender, DataEventArgs e) {
		print("buff length: " + e.Length + ", offset: " + e.Offset);
		
		if (e.Length <= 0) {
			return;
		}
		
		Array.Copy(e.Data, 0, buffer.Data, buffer.Length, e.Length);
		buffer.Length += e.Length;
		
		CodedInputStream stream = CodedInputStream.CreateInstance(buffer.Data);
		while (!stream.IsAtEnd) {
			int markReadIndex = (int)stream.Position;
			int varint32 = (int)stream.ReadRawVarint32();
			
			if (varint32 <= (buffer.Length - (int)stream.Position) ) {
				try {
					byte[] body = stream.ReadRawBytes (varint32);
					
					NamePacket response = NamePacket.ParseFrom(body);                      
					print("Response: " + response.ToString() + ", count: " + (++count));					
				} catch(Exception exception) {
					print(exception.Message);
				}
			} else {
				byte[] dest = new byte[8192];
				int remainSize = buffer.Length - markReadIndex;
				Array.Copy(buffer.Data, markReadIndex, dest, 0, remainSize);
				
				buffer.Data = dest;
				buffer.Offset = 0;
				buffer.Length = remainSize;
				
				break;
			}
		}
	}
}















