//package cancan.bledemo.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import cancan.bledemo.R;
//import cancan.bledemo.model.ItemCharacter;
//
///**
// * 描述：
// * 作者：Wu on 2017/4/5 22:55
// * 邮箱：wuwende@live.cn
// */
//
//public class DeviceDetailAdapter extends BaseExpandableListAdapter {
//
//
//    private Context mContext;
//
//    private List<UUID> groupArray;
//    private List<List<ItemCharacter>> childArray;
//
//
//
//    public DeviceDetailAdapter(Context mContext) {
//        this.mContext = mContext;
//        groupArray = new ArrayList<>();
//        childArray = new ArrayList<>();
//    }
//
//
//    public void setData(List<UUID> gArray ,List<List<ItemCharacter>> cArray) {
//        groupArray.clear();
//        childArray.clear();
//        groupArray.addAll(gArray);
//        childArray.addAll(cArray);
//        notifyDataSetChanged();
//    }
//
//
//
///**-----------------------------------子项------------------------------------------------------------------------------*/
//
//
//    @Override
//    public int getChildrenCount(int i) {//  获得父项子类的数量
//        return childArray.get(i).size();
//    }
//
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
//        View view = convertView;
//        ChildHolder childHolder = null;
//        if (view == null) {
//            childHolder = new ChildHolder();
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_child_list, null);
//            childHolder.childName = (TextView)view.findViewById(R.id.tv_child);
//            view.setTag(childHolder);
//        }else{
//            childHolder = (ChildHolder)view.getTag();
//        }
//
//        String p=null;
//        int  property = childArray.get(groupPosition).get(childPosition).getProprty();
//        if(property ==16){
//            p = "NOTIFY";
//        }else if(property == 18){
//
//        }
//
//
//        childHolder.childName.setText("Characteristic:" + "\n" + childArray.get(groupPosition).get(childPosition).getcUuid().toString()
//                 + "\n" + "property:"  + p);
//
//        return view;
//    }
//
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) { //  获得某个父项的某个子项
//        return childArray.get(groupPosition).get(childPosition);
//    }
//
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {//  获得子项的id
//        return childPosition;
//    }
//
//
//    @Override
//    public boolean isChildSelectable(int i, int i1) {  //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
//        return true;
//    }
//
//    /**-----------------------------------父项------------------------------------------------------------------------------*/
//
//    @Override
//    public int getGroupCount() {//  获得父项的数量
//        return groupArray.size();
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {//  获得某个父项
//        return childArray.get(groupPosition);
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
//        View view = convertView;
//        GroupHolder holder = null;
//        if(view == null){
//            holder = new GroupHolder();
//            view = LayoutInflater.from(mContext).inflate(R.layout.item_parent_list, null);
//            holder.groupName = (TextView)view.findViewById(R.id.tv_parent);
//            view.setTag(holder);
//        }else{
//            holder = (GroupHolder)view.getTag();
//        }
//
////        //判断是否已经打开列表
////        if(isExpanded){
////            holder.arrow.setBackgroundResource(R.drawable.dowm_arrow);
////        }else{
////            holder.arrow.setBackgroundResource(R.drawable.right_arrow);
////        }
////
//        holder.groupName.setText("Service:" + "\n" + groupArray.get(groupPosition).toString());
//
//        return view;
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {//  获得父项的id
//        return groupPosition;
//    }
//
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//
///**-------------------Viewholder----------------------------------------------------------------------------------------------*/
//
//    class GroupHolder{
//        public TextView groupName;
//    }
//
//    class ChildHolder{
//        public TextView childName;
//    }
//
//}
