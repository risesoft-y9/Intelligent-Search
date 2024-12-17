package net.risesoft.nosql.elastic.repository;

import net.risesoft.nosql.elastic.entity.Article.ArticleInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleInfoRepository extends ElasticsearchRepository<ArticleInfo,String> {

    List<ArticleInfo> findByDataType(String dataType);

}
