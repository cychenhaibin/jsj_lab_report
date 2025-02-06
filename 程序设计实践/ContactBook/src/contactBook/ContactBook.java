package contactBook;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class ContactBook {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Contact> searchResult = new ArrayList<>();
    private String searchName = "";
    private String dataFilePath = "data.txt";

    public ContactBook() {
        loadContactsFromFile(dataFilePath);
    }

    //�����ϵ��
    public boolean addContact(Contact contact) {
        if (isPhoneUsed(contact.getPhone(), "null")) {
            return false;
        }
        contacts.add(contact);
        saveContactsToFile(dataFilePath);
        return true;
    }

    //ͨ������������ϵ��
    public void searchContactByName(String name) {
        if (searchName.equals(name)) {
            return;
        }
        searchResult.clear();
        searchName = name;
        for (Contact contact : contacts) {
            if (contact.getName().contains(name)) {
                searchResult.add(contact);
            }
        }
    }

    //ͨ���绰����������ϵ��
    private Contact searchContactByPhone(String oldPhone) {
        for (Contact contact : contacts) {
            if (contact.getPhone().equals(oldPhone)) {
                return contact;
            }
        }
        return null;
    }

    //��ȡ������ϵ����Ϣ
    public Contact getContactByPhone(String phone) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (phone.equals(contact.getPhone())) {
                return contact;
            }
        }
        return new Contact();
    }

    //�༭��ϵ��
    public boolean editContact(String OldPhone, Contact contact) {
        if (isPhoneUsed(contact.getPhone(), OldPhone)) {
            return false;
        }
        Contact oldContact = searchContactByPhone(OldPhone);
        if (oldContact == null) {
            return false;
        }
        oldContact.setName(contact.getName());
        oldContact.setPhone(contact.getPhone());
        oldContact.setEmail(contact.getEmail());
        oldContact.setAddress(contact.getAddress());

        saveContactsToFile(dataFilePath);
        return true;
    }

    //ɾ������ϵ��
    public boolean deleteContact(String[] phones) {
        for (int i = 0; i < phones.length; i++) {
            Contact contact = searchContactByPhone(phones[i]);
            if (contact != null) {
                contacts.remove(contact);
            }
        }
        saveContactsToFile(dataFilePath);
        return true;
    }

    //����ֻ����Ƿ��Ѿ�����
    public boolean isPhoneUsed(String phone, String oldPhone) {
        if (phone.equals(oldPhone)) {
            return false;
        }
        for (Contact contact : contacts) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    // ���ļ���ȡ��ϵ����Ϣ��ArrayList
    public void loadContactsFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String phone = parts[1].trim();
                    String email = parts[2].trim();
                    String address = parts[3].trim();
                    Contact contact = new Contact(name, phone, email, address);
                    contacts.add(contact);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ��ArrayList�е���ϵ����Ϣ���浽�ļ�
    public void saveContactsToFile(String filename) {
        try {
            File file = new File(filename);
            PrintWriter writer = new PrintWriter(file);
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                writer.write(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail() + "," + contact.getAddress());
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TableData getContacts(String searchText, int pageNum, int pageSize) {
        Vector<Vector<Object>> data = new Vector<>();
        if (contacts == null) {
            return new TableData(0, data);
        }

        if (!searchText.equals("")) {
            searchContactByName(searchText);
            return getTableData(pageNum, pageSize, data, searchResult);
        } else {
            return getTableData(pageNum, pageSize, data, contacts);
        }
    }

    private TableData getTableData(int pageNum, int pageSize, Vector<Vector<Object>> data, ArrayList<Contact> contacts) {
        for (int i = (pageNum - 1) * pageSize; i < pageNum * pageSize && i < contacts.size(); i++) {
            Vector<Object> row = new Vector<>();
            Contact contact = contacts.get(i);
            row.addElement(i + 1);
            row.addElement(contact.getName());
            row.addElement(contact.getPhone());
            row.addElement(contact.getEmail());
            row.addElement(contact.getAddress());
            data.addElement(row);
        }
        return new TableData(contacts.size(), data);
    }


}
