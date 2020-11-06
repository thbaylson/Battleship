<<<<<<< HEAD:project3/MessageListener.java
=======
/**
 * Authors: Tyler Baylson & Dillion Gorlesky
 * Instructor: Dr. Scott Barlowe
 * Date: November 2020
 */

package common;

>>>>>>> 1c9c6cc0a30786e5fc6890d2280eb42d7a36a7f2:project3/common/MessageListener.java
/**
 * Defines the interface for objects that can observe other objects that receive messages. When
 * the subject receives a message, the message is forwarded to all registered observers.
 * This interface represents "observers" of MessageSources.
 *
 * @author Dr. William Kreahling
 * @version November 2017
 */
public interface MessageListener {
    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source);


    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The MessageSource that does not expect more messages.
     */
    public void sourceClosed(MessageSource source);
}