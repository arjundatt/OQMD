package DIC.Test.ChatClient;
public enum ClientType {
    zigbee{
        @Override
        public String toString() {
            return "zigbee";
        }
    },
    bluetooth{
        @Override
        public String toString() {
            return "bluetooth";
        }
    },
    rfid {
        @Override
        public String toString() {
            return "rfid";
        }
    },
    ethernet {
        @Override
        public String toString() {
            return "ethernet";
        }
    };
}
