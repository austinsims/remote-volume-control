using System;
using System.Net.Sockets;
using System.Text;

namespace RemoteVolumeControl
{
    class Program
    {
        static VolumeControl volumeControl_ = new VolumeControl();

        static void Up()
        {
            volumeControl_.VolUp();
            Console.WriteLine("Increased volume");
        }

        static void Down()
        {
            volumeControl_.VolDown();
            Console.WriteLine("Decreased volume");
        }

        static void Mute()
        {
            volumeControl_.Mute();
            Console.WriteLine("Muted");
        }

        static void Do(char ch)
        {
            if (ch == 'u')
            {
                Up();
            } else if (ch == 'd')
            {
                Down();
            } else if (ch == 'm')
            {
                Mute();
            }
        }

        static void Main(string[] args)
        {
            TcpListener serverSocket = new TcpListener(8001);
            int requestCount = 0;
            TcpClient clientSocket = default(TcpClient);
            serverSocket.Start();
            Console.WriteLine(" >> Server Started");
            clientSocket = serverSocket.AcceptTcpClient();
            Console.WriteLine(" >> Accept connection from client");
            requestCount = 0;

            while ((true))
            {
                try
                {
                    requestCount = requestCount + 1;
                    NetworkStream networkStream = clientSocket.GetStream();
                    byte[] bytesFrom = new byte[65536];
                    networkStream.Read(bytesFrom, 0, (int)clientSocket.ReceiveBufferSize);
                    char[] commands = Encoding.UTF8.GetString(bytesFrom).ToCharArray();
                    foreach (char c in commands)
                    {
                        Do(c);
                    }
                    /*
                    string serverResponse = "Last Message from client" + dataFromClient;
                    Byte[] sendBytes = Encoding.ASCII.GetBytes(serverResponse);
                    networkStream.Write(sendBytes, 0, sendBytes.Length);
                    networkStream.Flush();
                    Console.WriteLine(" >> " + serverResponse);
                    */
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.ToString());
                }
            }

            clientSocket.Close();
            serverSocket.Stop();
            Console.WriteLine(" >> exit");
            Console.ReadLine();
        }

    }
}
