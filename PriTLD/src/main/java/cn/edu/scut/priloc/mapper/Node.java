package cn.edu.scut.priloc.mapper;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;



public class Node<V> implements Serializable {
    private List<Long> keys;
    private List<Node<V>> children;
    private boolean isLeaf;
    private Node<V> previousNode;
    private Node<V> nextNode;
    private Node<V> parentNode;
    private List<Entry<V>> entrys;

    public void addChild(Node<V> child){
        children.add(child);
    }
    public void addChild(Node<V> child, int index){
        children.add(index,child);
    }
    public void sortAdd( Entry entry){
        //添位置
        getKeys().add(entry.getKey());
        getEntrys().add(entry);
        int size =getKeysSize() - 1;
        if (size == 0) {
            return;
        }
        //找位置
        int index = size;
        while (getKeys().get(size) < getKeys().get(index-1)) {
            index--;
            if (index <= 0) {
                break;
            }
        }
        //进入位置
        for (int i = size; i > index; i--) {
            getKeys().set(i, getKeys().get(i - 1));
            getEntrys().set(i, getEntrys().get(i - 1));
        }
        getKeys().set(index, entry.getKey());
        getEntrys().set(index, entry);
        return ;
    }

    public void addKey(Long key){
        keys.add(key);
    }
    public void addKey(Long key,int index){
        keys.add(index,key);
    }
    public void addEntry(Entry entry){
        entrys.add(entry);
    }
    public void addEntry(Entry entry, int index){
        entrys.add(index,entry);
    }

    public Node() {
        this.keys =new LinkedList<>();
        this.children=new LinkedList<>();
        this.entrys=new LinkedList<>();
        this.isLeaf=true;
    }

    //返回子节点大小
    public int getChildrenSize(){
        return this.children.size();
    }

    //返回键值大小
    public int getKeysSize(){
        return this.keys.size();
    }

    public Node getNextNode()
    {
        return nextNode;
    }
    public boolean isLeaf()
    {
        return isLeaf;
    }
    //获取指定下标的key
    public Long keyAt(int index){
        return keys.get(index);
    }

    //获取指定下标的entry
    public Entry<V> entryAt(int index){
        return entrys.get(index);
    }

    //获取指定下标的child
    public Node<V> childAt(int index){
        return children.get(index);
    }

    //更新父节点指定下标的key
    public void updateKey(int index,Long key){
        this.keys.set(index,key);
    }

    //删除指定节点key
    public void deleteKey(int index){
        this.keys.remove(index);
    }

    //删除指定节点child
    public void deleteChild(int index){
        this.children.remove(index);
    }

    //删除指定节点的entry
    public void deleteEntry(int index){
        this.entrys.remove(index);
    }

    //判断当前节点是否包含key 存在返回true
    public boolean contains(Long key){
        if(keys.contains(key)){
            return true;
        }
        return false;
    }

    //二分查找所查找key应该在节点哪
    public Result<V> search(Long key ){
        int begin=0;
        int end=this.getKeysSize()-1;
        int mid;
        int index=-1;
        Entry<V> entry=null;
        //node为空直接返回
        if(this.getKeysSize() <=0){
            return new Result<>(null,entry,index,false);
        }
        //若节点为叶子节点且不存在
//        if(this.isLeaf() && ! this.getKeys().contains(key)){
//            return new Result<>(null,entry,index,false);
//        }
        while(begin<end){
            mid=(begin+end)/2;
            int com = 0;
            if(this.keyAt(mid).longValue() > key){
                com = 1;
            }else if(this.keyAt(mid).longValue() < key){
                com = -1;
            }else{
                com = 0;
            }
            if(com==0){
                //中间值为所查找值
                index=mid;
                if(this.isLeaf()){
                    entry=this.entryAt(mid);
                }
                return new Result<>(key,entry,index,true);
            }else if(com <0){
                //key大
                begin=mid+1;
            }else{
                //mid大
                end=mid-1;
            }
        }
        int com = 0;
        if(this.keyAt(begin).longValue() > key){
            com = 1;
        }else if(this.keyAt(begin).longValue() < key){
            com = -1;
        }else{
            com = 0;
        }
        if(com == 0){
            index=begin;
            if(this.isLeaf()){
                entry=this.entryAt(begin);
            }
            return new Result<>(key,entry,index,true);
        }else if(com < 0){
            //begin所在位置key小与查询key
            index=begin;
            if(this.isLeaf()){
                //如果不是叶子节点，子节点下边等同begin，如果是叶子节点，插入需要+1，因为这种B+树子节点少一个头
                index=begin+1;
            }
            return new Result<>(key,null,index,false);
        }else{
            //begin所在位置key大于查询key
            index=begin;
            return new Result<>(key,null,index,false);
        }
    }

    public List<Long> getKeys() {
        return keys;
    }

    public void setKeys(List<Long> keys) {
        this.keys = keys;
    }

    public List<Node<V>> getChildren() {
        return children;
    }

    public void setChildren(List<Node<V>> children) {
        this.children = children;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Node<V> getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node<V> previousNode) {
        this.previousNode = previousNode;
    }

    public void setNextNode(Node<V> nextNode) {
        this.nextNode = nextNode;
    }

    public Node<V> getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node<V> parentNode) {
        this.parentNode = parentNode;
    }

    public List<Entry<V>> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<Entry<V>> entrys) {
        this.entrys = entrys;
    }

    @Override
    public String toString() {
        return "Node{" +
                "keys=" + keys +
                ", children=" + children +
                ", isLeaf=" + isLeaf +
                ", previousNode=" + previousNode +
                ", nextNode=" + nextNode +
                ", parentNode=" + parentNode +
                ", entrys=" + entrys +
                '}';
    }
}
