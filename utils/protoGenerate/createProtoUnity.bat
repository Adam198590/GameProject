@echo Creating of proto class for C#/Unity

start "" protoc.exe --proto_path=..\.. --descriptor_set_out=BinProto.protobin ..\..\repository\GameServer\res\protoconf\BasePacketsProto.proto

start "" Google.ProtocolBuffers.2.4.1.555\tools\ProtoGen.exe BinProto.protobin

::@pause