package tesseract.core.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;
import tesseract.exception.TesseractException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: hessian序列化实现
 * @author: nickle
 * @create: 2019-09-09 10:53
 **/
@Slf4j
public class HessianSerializerService implements ISerializerService {
    @Override
    public byte[] serialize(Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("序列化对象: {}", obj.toString());
            throw new TesseractException("序列化异常: " + e.getMessage());
        } finally {
            try {
                if (hessianOutput != null) {
                    hessianOutput.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessianInput = new HessianInput(byteArrayInputStream);
            return hessianInput.readObject();
        } catch (IOException e) {
            log.error("反序列化遭遇异常，字节转为String: {}", new String(bytes, 0, bytes.length));
            throw new TesseractException("反序列化异常: " + e.getMessage());
        } finally {
            try {
                if (hessianInput != null) {
                    hessianInput.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
