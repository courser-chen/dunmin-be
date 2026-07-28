package com.dunmin.cfg.service;

import com.dunmin.cfg.repository.FormFieldMapper;
import com.dunmin.cfg.repository.FormMapper;
import com.dunmin.model.entity.config.Form;
import com.dunmin.model.entity.config.FormField;
import com.dunmin.repository.service.BaseServiceImpl;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表单 Service
 */
@Service
public class FormService extends BaseServiceImpl<Form> {

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private FormFieldMapper formFieldMapper;

    @Override
    protected BaseMapper<Form> getMapper() {
        return formMapper;
    }

    @Override
    protected Class<Form> getEntityClass() {
        return Form.class;
    }

    /**
     * 分页查询
     */
    public Page<Form> page(int page, int pageSize, String keyword) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(Form.class);

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and("name like ? or code like ?",
                    "%" + keyword + "%",
                    "%" + keyword + "%");
        }

        queryWrapper.orderBy("id desc");
        return super.page(page, pageSize, queryWrapper);
    }

    /**
     * 查询所有
     */
    public List<Form> listAll() {
        return listByQuery(
            QueryWrapper.create()
                .from(Form.class)
                .orderBy("id desc")
        );
    }

    /**
     * 根据编码查询
     */
    public Form getByCode(String code) {
        return getOneByQuery(
            QueryWrapper.create()
                .from(Form.class)
                .and("code = ?", code)
        );
    }

    /**
     * 检查编码是否存在
     */
    public boolean existsByCode(String code) {
        return getByCode(code) != null;
    }

    // ==================== FormField 业务方法 ====================

    /**
     * 查询表单的所有字段
     */
    public List<FormField> listFields(Long formId) {
        return formFieldMapper.selectListByQuery(
            QueryWrapper.create()
                .from(FormField.class)
                .and("form_id = ?", formId)
                .orderBy("sort asc")
        );
    }

    /**
     * 获取单个字段
     */
    public FormField getField(Long id) {
        return formFieldMapper.selectOneById(id);
    }

    /**
     * 保存字段（新增或修改）
     */
    public boolean saveField(FormField field) {
        if (field.getId() != null) {
            return formFieldMapper.update(field) > 0;
        } else {
            return formFieldMapper.insert(field, true) > 0;
        }
    }

    /**
     * 删除字段（逻辑删除）
     */
    public boolean removeField(Long id) {
        FormField field = getField(id);
        if (field != null) {
            field.setDr(1);
            return formFieldMapper.update(field) > 0;
        }
        return false;
    }

    /**
     * 物理删除字段
     */
    public boolean deleteField(Long id) {
        return formFieldMapper.deleteById(id) > 0;
    }

    /**
     * 根据表单ID删除所有字段（逻辑删除）
     */
    public boolean removeFieldsByFormId(Long formId) {
        List<FormField> fields = listFields(formId);
        boolean success = true;
        for (FormField field : fields) {
            if (!removeField(field.getId())) {
                success = false;
            }
        }
        return success;
    }
}
