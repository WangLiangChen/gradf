package liangchen.wang.gradf.framework.cluster.jgroups;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.HostUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.commons.utils.RandomUtil;
import org.jgroups.*;
import org.jgroups.jmx.JmxConfigurator;
import org.jgroups.util.Util;

import javax.management.MBeanServer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public enum BroadcastingCluster {
    //
    INSTANCE;
    private final String clusterName = "CrdfBroadcastingCluster";
    private final Map<String, String> states = new HashMap<>();
    private final JChannel channel;
    private final String channelName;

    BroadcastingCluster() {
        channelName = String.format("%s@%d", HostUtil.INSTANCE.hostName(), RandomUtil.INSTANCE.random(10000, 99999));
        try {
            channel = new JChannel();
            channel.setName(channelName);
            channel.setDiscardOwnMessages(true);
            channel.receiver(new DefaultReceiver());
            channel.addChannelListener(new DefaultChannelListener());
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public void start() {
        // connect and getState
        Printer.INSTANCE.prettyPrint("JChannel:{} is starting", channelName);
        try {
            channel.connect(clusterName, null, 30000);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        Printer.INSTANCE.prettyPrint("JChannel:{} is started", channelName);
    }

    public void close() {
        channel.close();
        Printer.INSTANCE.prettyPrint("JChannel:{} is closed", channelName);
    }

    public void broadcast(byte[] bytes) {
        try {
            channel.send(null, bytes);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    public void broadcast(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        broadcast(bytes);
    }

    class DefaultReceiver extends ReceiverAdapter {
        @Override
        public void receive(Message msg) {
            System.out.println("===>receive:" + new String(msg.getBuffer(), StandardCharsets.UTF_8));
        }

        @Override
        public void getState(OutputStream output) {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(output, 1000));
            try {
                synchronized (states) {
                    Util.objectToStream(states, out);
                    Printer.INSTANCE.prettyPrint("JChannel:{} getState:{}", channelName, states.toString());
                }
            } catch (IOException e) {
                throw new ErrorException(e);
            } finally {
                Util.close(out);
            }
        }

        @Override
        public void setState(InputStream input) {
            try {
                Map<String, String> copy = Util.objectFromStream(new DataInputStream(input));
                Printer.INSTANCE.prettyPrint("JChannel:{} setState:{}", channelName, copy.toString());
                synchronized (states) {
                    states.clear();
                    states.putAll(copy);
                }
            } catch (Exception e) {
                throw new ErrorException(e);
            } finally {
                Util.close(input);
            }
        }

        @Override
        public void viewAccepted(View view) {
            Printer.INSTANCE.prettyPrint("JChannel:{} viewAccepted:{}", channelName, view.getViewId());
        }
    }

    class DefaultChannelListener implements ChannelListener {

        @Override
        public void channelConnected(JChannel jChannel) {
            // 注册JMX
            Util.registerChannel(channel, clusterName);
        }

        @Override
        public void channelDisconnected(JChannel jChannel) {
            // 注销JMX
            MBeanServer server = Util.getMBeanServer();
            if (server != null) {
                try {
                    JmxConfigurator.unregisterChannel(channel, server, clusterName);
                } catch (Exception e) {
                    throw new ErrorException(e);
                }
            }
        }

        @Override
        public void channelClosed(JChannel jChannel) {
            Printer.INSTANCE.prettyPrint("JChannel::{} is closed!", jChannel.getName());
        }
    }
}
