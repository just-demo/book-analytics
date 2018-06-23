package edu.self.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.self.model.user.UserWords;

import static edu.self.utils.Config.CONFIG_ROOT;

public class XMLUtils {

    public static void main(String[] args) throws FileNotFoundException, JAXBException {
//		GoogleTranslations googleTranslations = XMLUtils.load(CONFIG_ROOT + "\\google\\translations.xml", GoogleTranslations.class);
//		System.out.println(googleTranslations);
        UserWords words = XMLUtils.load(CONFIG_ROOT + "\\user\\words.xml", UserWords.class);
        System.out.println(words);
    }

    public static <T> T load(String fileName, Class<T> clazz) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new FileInputStream(fileName));
    }

    public static void save(String fileName, Object object) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, new FileOutputStream(fileName));
    }

//	public static void save(String fileName, Object object, Class ... clazz) throws JAXBException, FileNotFoundException {
//		List<Class> classes = new ArrayList<Class>(Arrays.asList(clazz));
//		classes.add(object.getClass());
//		JAXBContext context = JAXBContext.newInstance(classes.toArray(new Class[]{}));
//		Marshaller marshaller = context.createMarshaller();
//		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		marshaller.marshal(object, new FileOutputStream(fileName));
//	}
}
