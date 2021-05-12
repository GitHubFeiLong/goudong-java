
> 注意
>
> 我们在定义脚本的时候，除了 V 字开头的脚本之外，还有一种 R 字开头的脚本。
> + V 字开头的脚本只会执行一次。
> + R 字开头的脚本，只要脚本内容发生了变化，启动时候就会执行。
> + 所有的脚本，一旦执行了，就会在 flyway_schema_history 表中有记录，如果你不小心搞错了，可以手动从 flyway_schema_history 表中删除记录，然后修改 SQL 脚本后再重新启动（生产环境不建议）。

> 注意
>
>Flyway将每一个数据库脚本称之为：migrations，flyway支持三种类型的migration：
>+ Versioned migrations：最常用的migration，可以简单的理解为数据库升级脚本
>+ Undo migrations：数据库版本回退脚本，需要Pro版本，忽略，而且使用过程存在较大风险，undo操作目前只能通过plugin或者command-line来执行 
>+ Repeatable migrations：可重复执行的migration，例如create or replace脚本，当脚本checksums改变时会重新执行
  
 ## 配置属性
```yaml
spring:
  flyway:
    baseline-description: #执行基线时标记已有Schema的描述。
    baseline-on-migrate: false # 在没有元数据表的情况下，针对非空Schema执行迁移时是否自动调用基线。（默认值：false。）
    baseline-version: 1 #执行基线时用来标记已有Schema的版本。 （默认值：1。）
    check-location: false #检查迁移脚本所在的位置是否存在。 （默认值： false 。 ）
    clean-on-validation-error: false #在验证错误时，是否自动执行清理。 （默认值： false 。 ）
    enabled: true #开启Flyway。 （默认值： true 。 ）
    encoding: UTF-8 #设置SQL迁移文件的编码。 （默认值： UTF-8 。 ）
    ignore-failed-future-migration: false #在读元数据表时，是否忽略失败的后续迁移。 （默认值： false 。 ）
    init-sqls: #获取连接后立即执行初始化的SQL语句。
    locations: classpath:db/migration #迁移脚本的位置。 （默认值： db/migration 。 ）
    out-of-order: false #是否允许乱序（out of order）迁移。 （默认值： false 。 ）
    password: #待迁移数据库的登录密码
    placeholder-prefix: #设置每个占位符的前缀。 （默认值： ${ 。 ）
    placeholder-replacement: #是否要替换占位符。 （默认值： true 。 ）
    flyway.placeholder-suffix: #设置占位符的后缀。 （默认值： } 。 ）
    placeholders: [placeholder name] #设置占位符的值。
    schemas: #Flyway管理的Schema列表，区分大小写。默认连接对应的默认Schema。
    sql-migration-prefix: V #SQL迁移的文件名前缀。 （默认值： V 。 ）
    sql-migration-separator: __ #SQL迁移的文件名分隔符。 （默认值： __ 。 ）
    sql-migration-suffix: .sql #SQL迁移的文件名后缀。 （默认值： .sql 。 ）
    table: #Flyway使用的Schema元数据表名称。 （默认值： schema_version 。 ）
    target: #Flyway要迁移到的目标版本号。 （默认最新版本。 ）
    url: #待迁移的数据库的JDBC URL。如果没有设置，就使用配置的主数据源。
    user: #待迁移数据库的登录用户。
    validate-on-migrate: true #在运行迁移时是否要自动验证。 （默认值： true 。 ）
```


