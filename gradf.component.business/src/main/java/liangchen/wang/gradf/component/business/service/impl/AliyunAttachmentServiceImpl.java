package liangchen.wang.gradf.component.business.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import liangchen.wang.gradf.component.business.condition.AttachmentConditionAnnotation;
import liangchen.wang.gradf.component.business.enumeration.AttachmentStorageProvider;
import liangchen.wang.gradf.component.business.manager.IAttachmentManager;
import liangchen.wang.gradf.component.business.manager.IAttachmentReferenceManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AttachmentParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentReferenceResultDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentResultDomain;
import liangchen.wang.gradf.component.business.service.IAttachmentService;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.utils.ConfigurationUtil;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.FileUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.apache.commons.configuration2.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author LiangChen.Wang 2019/9/17 17:49
 */
@Service("Gradf_Commons_AliyunAttachmentService")
@AttachmentConditionAnnotation(havingValue = AttachmentStorageProvider.aliyun)
public final class AliyunAttachmentServiceImpl implements IAttachmentService {
    private final String uploadEndpoint;
    private final String uploadKeyId;
    private final String uploadKeySecret;
    private final String uploadBucket;
    private final String downloadEndpoint;
    private final String downloadPath;

    private final IAttachmentManager manager;
    private final IAttachmentReferenceManager attachmentReferenceManager;

    @Inject
    public AliyunAttachmentServiceImpl(@Named("Gradf_Business_DefaultAttachmentManager") IAttachmentManager manager,
                                       @Named("Gradf_Business_DefaultAttachmentReferenceManager") IAttachmentReferenceManager attachmentReferenceManager) {
        this.manager = manager;
        this.attachmentReferenceManager = attachmentReferenceManager;
        Configuration config = ConfigurationUtil.INSTANCE.getConfiguration("attachment.properties");
        uploadEndpoint = config.getString("aliyun.upload.endpoint");
        uploadKeyId = config.getString("aliyun.upload.keyId");
        uploadKeySecret = config.getString("aliyun.upload.keySecret");
        uploadBucket = config.getString("aliyun.upload.bucket");
        downloadEndpoint = config.getString("aliyun.download.endpoint");
        downloadPath = config.getString("aliyun.download.path");
    }

    @Override
    public Long upload(MultipartFile multipartFile) {
        try {
            return upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize());
        } catch (IOException e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public Long upload(InputStream intputStream, String originalFilename, String contentType, Long size) {
        String folder = DateTimeUtil.INSTANCE.getYYYYMM();
        Long attachment_id = UidDb.INSTANCE.uid();
        String extension = FileUtil.INSTANCE.getExtension(originalFilename).orElse("none");
        String fileName = String.format("%s/%d.%s", folder, attachment_id, extension);

        OSS ossClient = new OSSClientBuilder().build(uploadEndpoint, uploadKeyId, uploadKeySecret);
        try (InputStream inputStream = intputStream) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            ossClient.putObject(uploadBucket, fileName, inputStream, metadata);
        } catch (IOException e) {
            throw new ErrorException(e);
        } finally {
            ossClient.shutdown();
        }

        AttachmentParameterDomain parameterDomain = AttachmentParameterDomain.newInstance();
        parameterDomain.setAttachment_id(attachment_id);
        parameterDomain.setAttachment_name(originalFilename);
        parameterDomain.setAttachment_path(fileName);
        parameterDomain.setAttachment_size(size);
        parameterDomain.setAttachment_type(contentType);
        manager.insert(parameterDomain);
        return attachment_id;
    }

    @Override
    public String resolveUrl(Long attachment_id) {
        Assert.INSTANCE.notNull(attachment_id, "attachment_id不能为空");
        Assert.INSTANCE.isTrue(attachment_id > 0, AssertLevel.INFO, "attachment_id必须大于0");
        AttachmentResultDomain resultDomain = manager.byPrimaryKey(attachment_id, new String[]{Status.NORMAL.name()}, null, "", "attachment_path");
        if (resultDomain == null) {
            return "";
        }
        StringBuffer url = new StringBuffer();
        url.append(downloadEndpoint).append(downloadPath).append(resultDomain.getAttachment_path());
        return url.toString();
    }

    @Override
    public List<AttachmentReferenceResultDomain> byBusiness(Long business_id, String business_type) {
        List<AttachmentReferenceResultDomain> attachmentReferenceResultDomains = attachmentReferenceManager.byBusiness(business_id, business_type);
        attachmentReferenceResultDomains.forEach(e -> {
            String resolveUrl = resolveUrl(e.getAttachment_id());
            e.setAttachment_url(resolveUrl);
        });
        return attachmentReferenceResultDomains;
    }

    @Override
    public List<AttachmentReferenceResultDomain> byBusinessAndFlag(Long business_id, String business_type, String attachment_flag) {
        List<AttachmentReferenceResultDomain> attachmentReferenceResultDomains = attachmentReferenceManager.byBusinessAndFlag(business_id, business_type, attachment_flag);
        attachmentReferenceResultDomains.forEach(e -> {
            String resolveUrl = resolveUrl(e.getAttachment_id());
            e.setAttachment_url(resolveUrl);
        });
        return attachmentReferenceResultDomains;
    }

    public AttachmentResultDomain download(Long attachment_id) {
        return null;
    }

}
