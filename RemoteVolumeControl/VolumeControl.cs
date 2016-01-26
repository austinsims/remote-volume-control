using System.Runtime.InteropServices;

namespace RemoteVolumeControl
{
    public class VolumeControl
    {

        [DllImport("user32.dll")]
        static extern void keybd_event(byte bVk, byte bScan, uint dwFlags, int dwExtraInfo);

        public void Mute()
        {
            keybd_event(173, 0, 0, 0);
        }

        public void VolDown()
        {
            keybd_event(174, 0, 0, 0);
        }

        public void VolUp()
        {
            keybd_event(175, 0, 0, 0);
        }
    }
}