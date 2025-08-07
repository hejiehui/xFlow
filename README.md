# 简介
Xross workflow，简称为xflow，是一个轻量级工作流，包括IDEA编辑器和运行时。 

xflow利用本地文件来存储模型，其编辑和运行都无需依赖第三方服务器或数据库。

xflow支持丰富的活动节点和路由节点，支持子图调用，监听器和灵活的工作流相关操作。

<img width="1436" height="1160" alt="image" src="https://github.com/user-attachments/assets/ab3686c2-00fd-417c-a8b5-d4621b486d0e" />


# 快速体验

# 编辑器使用教程

## 安装
### 插件市场安装
在IDEA插件市场输入xross workflow即可。xflow依赖Xross Tools Graphic Edit Framework 1.4.1版本，注意同步更新。

### 手动安装
可以从github下载，直接拖入IDEA即可。
[插件安装包](https://github.com/hejiehui/xFlow/blob/main/com.xrosstools.xflow.idea.editor/com.xrosstools.xflow.idea.editor.zip)

### maven依赖

    <groupId>com.xrosstools</groupId>
    <artifactId>xflow</artifactId>
    <version>1.0.5</version>

## 创建模型
### 创建xflow模型文件
选中项目的resources目录，然后通过File->New->Xross Flow Model打开模型名字输入框，输入模型名字即可
<img width="547" height="851" alt="image" src="https://github.com/user-attachments/assets/dd92c10c-98d1-4de3-8787-491f00f2d0bc" />

### 编辑xflow模型文件
通过编辑窗口左侧的组件菜单来选择和放置相关节点，通过Link菜单来连接两个节点

右侧的outline窗口提供模型文件的结构，选中编辑窗口中的已有节点或连接，也会相应的选中在outline窗口，选中outline窗口中的元素，也会选中编辑窗口中的对应组件（可显示的）

## 概念简介
### 模型文件
一个xflow模型文件可以包括0到多个xflow模型，每个xflow模型都有一个单独的编辑窗口。模型文件一般存放在resources目录

### xfkow模型
一个个xflow模型可以包含多个节点，节点间可以有连线。xflow模型通过编辑器左侧Xflow菜单按钮进行创建。步骤是单击Xflow菜单，再单击编辑器主窗口空白处（非其他xflow模型子窗口内）

### 节点
节点分为：
* 开始节点：标记流程开始
* 结束节点：标记流程结束
* 活动节点：执行用户自定义或指定的逻辑
* 路由节点：对执行流程进行控制，选择后继执行路径

### 连接
用于连接节点，标志路径。

### 属性
模型文件，xflow模型，节点和连线都有对应的属性，选中后显示在下方属性窗口。

属性分为缺省属性和自定义属性。缺省属性和自定义属性命名空间各自独立。

例如下面模型文件属性为：
<img width="1157" height="171" alt="image" src="https://github.com/user-attachments/assets/e7c74037-7d67-4b07-8344-737ff63bfe30" />

其中Description为模型文件的缺省属性，Properties下面是模型文件级别的自定义属性。

#### 缺省属性
缺省属性由选中元素的种类来决定，由系统自动注入，用于构建xflow对应的运行时实例。

##### 模型文件级属性
仅包括可选的Description属性，既模型文件的全局描述。例子见上

##### xflow属性
* Id：xflow的唯一标识符
* Description：可选的xflow功能描述
* Listener：可选的监听器

例如：
<img width="1120" height="106" alt="image" src="https://github.com/user-attachments/assets/3e9350fb-1b7e-453d-971a-e1aeba42f5b8" />

#### 自定义属性
自定义属性由用户自己提供，用于对业务逻辑进行配置。xflow模型文件，每个xflow模型，每个节点都可以增加任意多自定义属性。可以通过实现特定接口来获得这些属性。

##### 全局属性接口

    package com.xrosstools.xflow;
    
    public interface GlobalConfigAware {
    	void initGlobalConfig(DataMap config);
    }

##### 流程属性接口
    package com.xrosstools.xflow;
    
    public interface FlowConfigAware {
    	void initFlowConfig(DataMap config);
    }

##### 节点属性接口

    package com.xrosstools.xflow;
    
    public interface NodeConfigAware {
    	void initNodeConfig(DataMap config);
    }

#### 设置Implementation属性
大多数活动节点和路由节点都包含Implementation属性，用于指向用户实现的业务逻辑。Implementation属性既可以在属性窗口直接输入，也可以右键点击节点，选择Assign Implementation，在对话框中选择所需实现类。

<img width="598" height="450" alt="image" src="https://github.com/user-attachments/assets/33c50214-fadc-41bd-aa90-bda1209af345" />

当Implementation属性设置好以后，可以通过右键菜单进行更改，或者跳转到相关实现类。

对于自动节点，二选一，多选一和多选多节点来说，除了可以实现相关接口，还可以指定某个方法作为实现，例如

<img width="740" height="495" alt="image" src="https://github.com/user-attachments/assets/2027c852-98f7-4df6-b7e8-41e43805f719" />

在类或方法上查找引用，可以定位到模型文件的引用。例如：

<img width="1161" height="955" alt="image" src="https://github.com/user-attachments/assets/1dad28c7-39fa-407d-b412-1a1ad9437499" />

xflow支持在类和方法重命名时自动修改模型。

#### Id属性的唯一性
xflow模型，节点和连线一般都包含Id属性，其中：
* xflow的ID在当前模型文件中需要唯一
* 节点ID需要仅需在当前xflow中唯一
* 连线ID仅需在相同源路由节点的连线中保持唯一

## 工作流实例状态
利用XflowFactory可以基于xflow模型文件创建工作流实例。实例状态为以下几种：

* CREATED：已创建，未执行
* RUNNING：运行状态，当前流程有活跃节点或失败节点
* SUSPENDED：暂停状态，由程序主动发起的suspend操作导致
* SUCCEED：流程执行到结束节点，流程成功结束
* FAILED：当前没有任何活跃或失败的节点，并且没有执行到结束节点
* ABORTED：废弃状态，由程序主动发起的abort操作导致

## 节点状态
节点有两个状态维度，分别是：
* isActive：是否活跃
* isFailed：是否失败

任一时间，一个节点只会有最多一个活动实例，因此节点对应的操作不会出现并发调用现象。

在节点处于活跃状态的时候，如果节点执行失败，节点将保持活跃，并进入失败状态，可以通过retry进行重试

# 节点

## 开始结束节点
Start和End节点分别为开始和结束节点。

xflow开始执行时会找到当前xflow模型中第一个Start节点并创建一个ActiveToken去调用该Start节点。

当任意节点后继执行到End节点时，xflow流程实例结束，当前所有活跃节点执行完毕后，不会继续执行后继节点。

## 活动节点
Xflow支持5种活动节点。分别是自动节点，任务节点，事件节点，延时等待节点和子流程节点。

除延时等待节点外，其他4个活动节点均需用户实现相应接口。活动节点通用属性如下所示：

<img width="1143" height="223" alt="image" src="https://github.com/user-attachments/assets/fd477cca-e5b1-4b9f-8b43-c1d99b37852a" />

* Id: 节点在当前模型中的唯一标识符，如果用户没有设置Label，则节点图形上将显示Id
* Label：可选节点标签。如果用户设置了该属性，则节点图形上将显示该值
* Description：可选节点描述
* Implementation：节点对应接口实现类，延时等待节点无此属性

如果用户没有为节点定义自定义属性，则将不显示Misc和Properties分类标志，反之上述属性将显示在Misc大类下，用户自定义属性将显示在Properties大类下

### 自动节点
Auto activity是自动节点，当流程实例执行到该节点，会调用AutoActivity接口实现：

    package com.xrosstools.xflow;
    
    public interface AutoActivity {
    	void execute(XflowContext context);
    }

例如：

<img width="1307" height="1187" alt="image" src="https://github.com/user-attachments/assets/e41182a7-4bf1-48c0-9b99-5289293adcba" />

    public class TestAutoActivity extends TestAdapter implements AutoActivity, NodeConfigAware, GlobalConfigAware {
    	public static final String PROP_KEY_COUNTER = "counter";
    	public static final String PROP_KEY_STEP = "step";
    
    	private int step;
    	
    	private DataMap config;
    	@Override
    	public void execute(XflowContext context) {
    		call(context);
    
    		AtomicInteger counter = context.get(PROP_KEY_COUNTER);
    		counter.addAndGet(step);

      		if(config.contains("globalA"))
    			context.copyFrom(config, "globalA", "globalB", "gBool");
    	}
    
    	@Override
    	public void initNodeConfig(DataMap config) {
    		step = config.get(PROP_KEY_STEP);
    	}
    
    	@Override
    	public void initGlobalConfig(DataMap config) {
    		this.config = config;
    	}
    }

备注：
实例代码中的用到的基类TestAdapter和调用的方法call(context)，以及下面会出现的injectException，injectSuspend等仅仅是为完成测试用的代码，与具体接口无关，请忽略。

### 任务节点
Task activity是任务节点，当流程实例执行到该节点，会调用TaskActivity接口实现：

    package com.xrosstools.xflow;
    
    import java.util.List;
    
    public interface TaskActivity {
    	/**
    	 * Create tasks that need to be handled
    	 * @param context the execution context
    	 * @return id that differentiates tasks created each time  
    	 */
    	List<Task> create(XflowContext context);
    
    	/**
    	 * Submit a task
    	 * @param context the execution context
    	 * @param tasks all the tasks that are generated by create method
    	 * @param task current task that user submitted
    	 * @return true if activity completed
    	 */
    	boolean submit(XflowContext context, List<Task> tasks, Task task);
    }

例如：

<img width="1297" height="936" alt="image" src="https://github.com/user-attachments/assets/349a8270-6f0c-4194-a67d-0a07440b28eb" />


    public class TestTaskActivity extends TestAdapter implements TaskActivity, NodeConfigAware, FlowConfigAware {
    	private int count;
    	private String assignee;
    
    	@Override
    	public void initFlowConfig(DataMap config) {
    		assignee = config.get("assignee");
    	}
    
    	@Override
    	public void initNodeConfig(DataMap config) {
    		count = config.get("count");		
    	}
    
    	@Override
    	public List<Task> create(XflowContext context) {
    		call(context);
    		List<Task> tasks = new ArrayList<>();
    		for(int i = 0; i < count; i++) {
    			tasks.add(new FeedbackTask("id_" + i, assignee));
    		}
    		System.out.println("Task generated: " + tasks.size());
    		return tasks;
    	}
    
    	@Override
    	public boolean submit(XflowContext context, List<Task> tasks, Task task) {
    		call(context);
    		return ((FeedbackTask)task).getFeedback().equals("OK");
    	}
    }

### 事件节点
Event activity是事件节点，当流程实例执行到该节点，会调用EventActivity接口实现：

    package com.xrosstools.xflow;
    
    public interface EventActivity {
    	EventSpec specify(XflowContext context);
    
    	void notify(XflowContext context, EventSpec spec, Event event);
    }

例如：

<img width="1355" height="1052" alt="image" src="https://github.com/user-attachments/assets/db2bed5a-5b41-4306-a93b-7274ffc80207" />

    public class TestEventActivity extends TestAdapter implements EventActivity, NodeConfigAware  {
    	public static final String EVENT_ID = "event id";
    	private String eventId;
    	@Override
    	public EventSpec specify(XflowContext context) {
    		call(context);
    		return new EventSpec(eventId);
    	}
    
    	@Override
    	public void notify(XflowContext context, EventSpec spec, Event event) {
    		call(context);
    		if(!event.getId().equals(eventId))
    			throw new IllegalArgumentException();
    	}
    
    	@Override
    	public void initNodeConfig(DataMap config) {
    		eventId = config.get(EVENT_ID);
    	}
    }


### 延时等待节点
Wait activity是延时等待节点，当流程实例执行到该节点，会等待给定时间再进入到后继节点。

其属性如下：

<img width="1171" height="190" alt="image" src="https://github.com/user-attachments/assets/fc90d4c4-16a6-40c3-a77a-37f528836f90" />

* Delay：是要延时的数值
* Time unit：延时数值对应的单位：

<img width="236" height="151" alt="image" src="https://github.com/user-attachments/assets/18b5a8b5-8224-4a51-84d3-18ed47e2c7d9" />

例如：

<img width="1326" height="995" alt="image" src="https://github.com/user-attachments/assets/d13dc899-2574-4fde-ba60-ea2723598945" />

### 子流程节点
Subflow activity是子流程节点节点，该节点属性如下：

<img width="1173" height="160" alt="image" src="https://github.com/user-attachments/assets/39e311cc-43a3-478a-a1a7-c0388ddc6de5" />

* Subflow：当前模型文件中某个xflow模型的ID

当前不支持跨模型文件的子流程调用，未来版本会支持

当流程实例执行到该节点，会调用SubflowActivity接口实现：

    package com.xrosstools.xflow;
    
    public interface SubflowActivity {
    	XflowContext createContext(XflowContext parentContext);
    	void mergeSubflow(XflowContext parentContext, XflowContext subFlowContext);
    }

例如：

<img width="1343" height="966" alt="image" src="https://github.com/user-attachments/assets/f18ef5ef-6557-4274-86bc-a4448c6065d2" />

    public class TestSubflowActivity extends TestAdapter implements SubflowActivity {
    	public static final String COUNT = "count";
    	public static final String ERROR = "error";
    
    	@Override
    	public XflowContext createContext(XflowContext parentContext) {
    		call(parentContext);
    
    		XflowContext subflowContext = new XflowContext();
    		
    		if(parentContext.contains(ERROR))
    			injectException(subflowContext, (Exception)subflowContext.get(ERROR));
    		
    		if(parentContext.contains(SUB_FLOW_SUSPEND))
    			injectSuspend(subflowContext, nodeStarted, START_NODE);
    		
    		int count = parentContext.get(COUNT);
    		subflowContext.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(count));
    		
    		return subflowContext;
    	}
    
    	@Override
    	public void mergeSubflow(XflowContext parentContext, XflowContext subFlowContext) {
    		call(parentContext);
    
    		AtomicInteger counter = subFlowContext.get(TestAutoActivity.PROP_KEY_COUNTER);
    		parentContext.put(COUNT, counter.get());
    	}
    }

## 路由节点
Xflow支持4种路由节点。分别是二选一路由节点，多选多路由节点，多选一路由节点，以及并发路由节点。

路由节点属性与活动节点类似，都是由Id，Lebel，Description和Implementation组成。除并发路由节点没有Implementation属性外。

和活动节点一样，路由节点可以配置用户自定义属性。

### 二选一路由节点
Binary router是二选一路由节点，当流程实例执行到该节点，会调用BinaryRouter接口实现：

    package com.xrosstools.xflow;
    
    public interface BinaryRouter {
        boolean route(XflowContext context);
    }

二选一路由节点只能有最多两条出边，并且其出边的Id只能是true/false二选一。

<img width="1152" height="120" alt="image" src="https://github.com/user-attachments/assets/cc61577b-4027-48f4-9ae4-8515f9b3c802" />

例如：

<img width="1355" height="874" alt="image" src="https://github.com/user-attachments/assets/646080c9-8c13-42f0-be1d-efb72a69a129" />

    public class TestBinaryRouter extends TestAdapter implements BinaryRouter {
        public static final String PROP_KEY_RESULT = "result";
        
        @Override
        public boolean route(XflowContext context) {
            call(context);
            return context.get(PROP_KEY_RESULT);
        }
    }

### 多选多路由节点
Inclusive router是多选多路由节点，当流程实例执行到该节点，会调用InclusiveRouter接口实现：

    package com.xrosstools.xflow;
    
    public interface InclusiveRouter {
        String[] route(XflowContext context);
    }

对于多选多路由节点的出边来说，其连接属性多了一个bool型的Default link：

<img width="1225" height="146" alt="image" src="https://github.com/user-attachments/assets/a7bad269-9214-4446-822f-3c989efe32ef" />

例如：

<img width="1320" height="903" alt="image" src="https://github.com/user-attachments/assets/e1eb6212-a12b-4dc2-a6b2-16dc7b76a8ac" />

    public class TestInclusiveRouter extends TestAdapter implements InclusiveRouter {
        public static final String PROP_KEY_PATHES = "pathes";
    
        @Override
        public String[] route(XflowContext context) {
            call(context);
            if(!context.contains(PROP_KEY_PATHES))
                return null;
    
            String pathes = context.get(PROP_KEY_PATHES);
            return pathes.split(",");
        }
    }



### 多选一路由节点
Exclusive router是多选一路由节点，当流程实例执行到该节点，会调用ExclusiveRouter接口实现：

    package com.xrosstools.xflow;
    
    public interface ExclusiveRouter {
    	String route(XflowContext context);
    }

例如：

<img width="1345" height="889" alt="image" src="https://github.com/user-attachments/assets/9a960f3b-a664-4b9f-aac7-3db7da517d6d" />

    public class TestExclusiveRouter extends TestAdapter implements ExclusiveRouter {
    	public static final String PROP_KEY_PATH = "path";
    
    	@Override
    	public String route(XflowContext context) {
    		call(context);
    		return context.get(PROP_KEY_PATH);
    	}
    }

### 并发路由节点
Parallel router是二选一路由节点，当流程实例执行到该节点，会并发调用与其相连的所有后继节点。Parallel router会自动执行，无需用户提供实现。

例如：

<img width="1167" height="820" alt="image" src="https://github.com/user-attachments/assets/3fb442fa-21f3-4f3e-b017-8c059d3f2f05" />

## 自定义属性
自定义属性可以在模型文件，xflow模型和节点这三个层面进行定义。

其中模型文件对应属性又可称为全局属性，xflow属性可称为流程属性。

每个属性都需要指定属性类型，目前属性类型支持String，Integer等基本数据类型和Date， Timeunit类型。

### 全局属性
为模型文件级别公共属性，所有该模型文件中的xflow都可以获取。要创建，删除和修改全局属性可在编辑器空白处（非某个xlfow模型窗口中），右键单击即可调出

例如：
<img width="787" height="664" alt="image" src="https://github.com/user-attachments/assets/f09f4079-44f2-4936-8bbc-75fe8bfcc6f1" />

选择属性对应的类型后，在对话框中输入属性名称，例如：

<img width="441" height="156" alt="image" src="https://github.com/user-attachments/assets/75a24919-8bca-4e29-84e1-3b583ec3a26f" />

### 流程属性
为该xflow特定属性，仅该xflow内部节点可以获取。要创建，删除和修改xflow属性可在xflow模型窗口空白处右键单击即可调出

例如：
<img width="794" height="551" alt="image" src="https://github.com/user-attachments/assets/c64aa1ac-56d2-4727-aed4-0d4fee2a64b6" />

### 节点属性
为该节点独有属性，其他节点无法获取。右键单击节点即可对属性进行创建，修改和删除操作。

例如：
<img width="747" height="467" alt="image" src="https://github.com/user-attachments/assets/d20e9b70-7b9e-4f40-bcdc-86c3c4375e15" />

#### 预定义属性名称
为避免输入属性名称过程中产生人为错误，可以通过预定义属性名称来让系统自动识别。

具体方法是在节点的实现类里面定义名字开头为：“PROP_KEY_” 的公共的字符变量。
例如：

    public static final String PROP_KEY_COUNTER = "counter";

此时系统即可对该属性进行识别：

<img width="771" height="478" alt="image" src="https://github.com/user-attachments/assets/68ec9d86-8052-4bc0-a91d-8cc8b56664e1" />

## 帮助类
在运行时，用户可以直接调用XflowFactioy完成读取模型文件，生成流程实例的工作。但为方便用户调用，避免对模型文件，流程和节点名称的硬编码，用户可以选择生成Helper类。

首先选择该类对应的源文件目录或要覆盖的旧文件，再单击Helper按钮，输入名称即可，例如
<img width="1308" height="904" alt="image" src="https://github.com/user-attachments/assets/b91f42bb-5a5a-47fa-a9a0-b2a6074acdc9" />

以spring_test.xflow为例生成的helper：

<img width="1301" height="1214" alt="image" src="https://github.com/user-attachments/assets/77c86b54-f871-492d-918c-c5038631c11b" />


生成的代码主体部分如下：

	public class SpringTest {
	    
	    //Diagram level user defined properties
	    public static final String GLOBAL_VAR_1 = "global var 1";
	
	    public static final String GOBAL_VAR_2 = "gobal var 2";
	
	    public static class AutoActivity {
	        //Xflow level user defined properties
	        public static final String FLOW_VAR_2 = "flow var 2";
	
	        public static final String FLOW_VAR_1 = "flow var 1";
	
	        /*  Node Names */
	        public static final String START = "start";
	
	        public static final String END = "end";
	
	        public static final String BBB = "a1";
	
	        public static Xflow create() {
	            return load().create("auto activity");
	        }
	    }
	
	
	    private static volatile XflowFactory factory;
	    private static XflowFactory load()  {
	        if(factory == null) {
	            synchronized(SpringTest.class) {
	                if(factory == null)
	                    factory = XflowFactory.load("spring_test.xflow");
	            }
	        }
	        return factory;
	    }
	}

### 代码结构

* Helper类：模型文件对应Java类
* 全局自定义配置名定义：变量名对应的常量
* 流程定义类：每个流程模型对应内部类
* 流程自定义配置名定义：变量名对应的常量
* 节点名称常量：该流程内部每个节点名称定义
* create方法：流程实例构造方法
* factory静态属性：工厂类实例
* load方法： 工程类实例构造方法

其中节点名常量由节点的Id和Label属性决定。如果没有配置Label属性，则常量名根据由Id产生，如果有Label属性，则由Label属性值产生。产生逻辑是将空格替换为下划线：_，再转大写。

# 模型调用

## 加载模型文件

    XflowFactory factory = XflowFactory.load("unit_test.xflow");

上面加载模型位于resources/xflow/unit_test.xflow。如果使用Helper，该步骤在load方法中调用。

## 创建流程上下文
XflowContext提供流程实例启动和后继运行所需数据上下文，该类继承自DataMap，并包括父流程活动令牌和当前流程实例，定义如下：

    package com.xrosstools.xflow;
    
    public class XflowContext extends DataMap {
    	private ActiveToken parentToken;
    	private Xflow flow;
    
    	public void setFlow(Xflow flow) {
    		this.flow = flow;
    	}
    	
    	public Xflow getFlow() {
    		return flow;
    	}
    
    	public ActiveToken getParentToken() {
    		return parentToken;
    	}
    
    	public void setParentToken(ActiveToken parentToken) {
    		this.parentToken = parentToken;
    	}
    }

DataMap是一个通用数据容器类，内部通过ConcurrentHashMap存储数据。

XflowContext创建示例：

    XflowContext context = new XflowContext();
    context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

## 创建流程实例
用流程id调用XflowFactory.create方法来创建流程实例。

    Xflow f = factory.create("auto activity");

推荐使用生成的Helper类，例如

    Xflow f = UnitTest.AutoActivity.create();

## 启动流程实例
用XflowContext实例去调用Xflow的start方法来启动流程实例。

    f.start(context);

## 查询流程实例状态
流程实例状态通过以下几个方法进行查询：
* getStatus：获得当前流程实例的状态
* isRunning：是否还在运行
* isSuspended：是否处于暂停状态
* isEnded：实例是否结束，当状态处于SUCCEED，FAILED或ABORTED时判断为结束
* isSucceed：流程实例是否成功，既流程执行抵达结束节点
* isFailed：流程实例是否失败，既无任何活动节点，也没到达结束节点
* isAbort：是否处于放弃状态，该状态由人为调用abort方法触发
* getAbortReason：返回abort原因

例如：

    f.getStatus();
    f.isRunning();
    f.isSuspended();
    f.isEnded();
    f.isSucced()；
1    f.isFailed();
    f.isAbort();

## 改变流程实例状态
在流程实例生命周期内，其状态可由下列方法改变：
* suspend：暂停流程实例运行，由用户决定何时调用。当前正在执行的节点不受影响，但所有当前执行节点完成后，后继节点进入等待状态
* resume：恢复流程实例运行，由用户决定何时调用。所有处于等待状态的节点将被调度执行
* succeed：流程成功结束，当流程实例抵达end节点时由系统自动调用
* abort：放弃实例继续执行，要提供放弃原因

例如：
    
    f.suspend();
    f.resume();
    f.abort(reason);

## 流程持久化
虽然Xflow不依赖数据库和第三方服务器，流程实例状态保存在内存，但对于有需要的场景，xflow也提供持久化的支持。主要是下面三个功能

### 流程实例Id
Xflow提供instanceId的getter和setter，以方便用户记录流程实例Id。该Id由用户负责提供。

### 保存流程实例
流程实例可以通过以下方法进行保存，保存前需要流程进入暂停状态，并且无活跃节点，否则报错

    public XflowRecorder specify()

例如：

    XflowRecorder recorder = f.specify();

在获取XflowRecorder，用户可以选择任意方式对其进行持久化。

注意流程实例使用的流程上下文XflowContext需用户自行持久化。

### 恢复流程实例
用户可以使用XflowRecorder来恢复流程实例，方法如下：

    public void restore(XflowContext context, XflowRecorder flowRecorder)

例如：

		f = UnitTest.AutoActivity.create();
		f.restore(context, recorder);

注意原实例使用的流程上下文XflowContext需用户自行读取或创建。

## 节点操作
对节点的操作基本都通过Xflow类代理。用户无法直接操作节点实例。

### 查询状态
* getActiveNodeIds：获取当前活跃节点列表
* getFailedNodeIds：获取当前失败节点列表
* getPendingNodeIds: 获取流程暂停后所有待执行节点列表
* isActive(nodeId)：查询给定节点是否活跃
* isFailed(nodeId)：查询给定节点是否失败
* getFailure(nodeId)：查询给定节点失败报错

例如：

    List<String> ids = subflow.getFailedNodeIds();
    List<String> ids = f.getActiveNodeIds();
    f.isActive(nodeId);
    f.isFailed(nodeId);
    Throwable e = f.getFailure(nodeId);

### 重试
当节点执行失败后，可以调用retry操作进行重试。

例如：

    f.retry(nodeId);

## 事件处理
当流程实例中存在事件活动，在执行到事件活动时，该节点会保持活跃，等待用户提交事件。具体分为查询和提交两部分：

### 查询事件规格
通过getEventSpecs方法来获取当前实例所有需要提交的事件规格（因可能存在多个事件节点）。或者通过getEventSpec获取给定事件节点的事件规范。

EventSpec本身是DataMap，用户创建时可以设置相关数据。

例如：
    
    List<EventSpec> specs = f.getEventSpecs();
    EventSpec spec = f.getEventSpec("event activity");

EventSpec包含事件节点Id，事件Id和可选的截止时间

### 通知事件发生
通过notify方法来提交事件

例如：

    Event event = specs.get(0).create();
    f.notify(event);

## 任务处理
当流程实例中存在任务活动，在执行到任务活动时，该节点会保持活跃，等待用户提交任务。具体分为查询和提交两部分：

### 查询任务
可以从任务负责人和任务节点两个维度查询任务：
* getTasks：按照给定的任务assignee来查找所有当前该处理任务
* getNodeTasks：按照给定的任务节点名来查找所有当前节点该处理的任务

例如：

    List<Task> tasks = f.getTasks("Jerry");
    List<Task> nodeTasks = f.getNodeTasks("aaaabbb");

## 查询子流程
xflow通过下列两个方法获取子流程状态
* getSubflowContext(nodeId)：获取给定子流程节点所使用上下文
* getSubflow(nodeId)：获取给定子流程节点对应子流程实例

## Spring支持
用户可以通过设置XflowSpring来支持Spring，具体有两种方式。

方式一，定义返回XflowSpring实例的Bean工厂方法。例如：

	@Configuration
	@ComponentScan
	public class SpringBeanFactoryTest extends TestAdapter {
	
	    @Bean
	    XflowSpring createFactory() {
	    	return new XflowSpring();
	    }
	    
	    @BeforeClass
	    public static void setup() throws Exception {
	        ApplicationContext context = new AnnotationConfigApplicationContext(SpringBeanFactoryTest.class);
	    }

案例代码参考 [SpringBeanFactoryTest](https://github.com/hejiehui/xFlow/blob/main/com.xrosstools.xflow.sample/src/test/java/com/xrosstools/xflow/sample/spring/SpringBeanFactoryTest.java)

方式二，手工初始化XflowSpring类。例如：

	@Configuration
	@ComponentScan
	public class SpringDeclareTest extends TestAdapter {
	    @BeforeClass
	    public static void setup() throws Exception {
	        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDeclareTest.class);
	        //The following can be removed. Because spring will auto find this class
	        XflowSpring.enable(context);
		}
	    
案例代码参考 [SpringDeclareTest](https://github.com/hejiehui/xFlow/blob/main/com.xrosstools.xflow.sample/src/test/java/com/xrosstools/xflow/sample/spring/SpringDeclareTest.java)

## 监听器
XflowListener定义了流程实例监听器所记录的事件，主要分为
* 流程实例生命周期事件，如创建，启动，暂停等
* 节点活动状态如开始，成功，失败，重试等
* 节点操作如事件提交，任务提交和子流程提交等

具体可参考代码：[XflowListener](https://github.com/hejiehui/xFlow/blob/main/com.xrosstools.xflow/src/main/java/com/xrosstools/xflow/XflowListener.java)

用户可以继承XflowListenerAdapter并覆盖感兴趣的方法来加速开发自己的监听器。XflowSystemOutListener是XflowListener的一个简单实现，方便用户项目初期进行模型调试。
