package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wall
 * @data - 14:25
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;


    @RequestMapping("/index")
    public String index() {
        return "permission/index";
    }


    @RequestMapping("/toAdd")
    public String toAdd() {
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
        Permission permission = permissionService.getPermissionById(id);
        map.put("permission", permission);
        return "permission/update";
    }

    @ResponseBody
    @RequestMapping("/deletePermission")
    public Object deletePermission(Integer id){
        AjaxRusult result = new AjaxRusult();

        try {
            int count = permissionService.deletePermission(id);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("刪除许可树数据失败!");
        }
        return result ;
    }



    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Permission permission){
        AjaxRusult result = new AjaxRusult();

        try {
            int count = permissionService.updatePermission(permission);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("修改许可树数据失败!");
        }
        return result ;
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Permission permission){
        AjaxRusult result = new AjaxRusult();

        try {
            int count = permissionService.savePermission(permission);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存许可树数据失败!");
        }
        return result ;
    }
   //Demo5 - 用Map集合来查找父,来组合父子关系.减少循环的次数 ,提高性能.
   /* @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxRusult result = new AjaxRusult();

        try {
            List<Permission> root = new ArrayList<Permission>();

            List<Permission> childredPermissons =  permissionService.queryAllPermission();

            Map<Integer,Permission> map = new HashMap<Integer,Permission>();

            for (Permission innerpermission : childredPermissons) {
                map.put(innerpermission.getId(), innerpermission);
            }

            for (Permission permission : childredPermissons) {
                //通过子查找父
                //子菜单
                Permission child = permission ; //假设为子菜单
                if(child.getPid() == null ){
                    root.add(permission);
                }else{
                    //父节点
                    Permission parent = map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }
            result.setSuccess(true);
            result.setData(root);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败!");
        }

        return result ;
    }
*/

    //Demo4 - 采用一次性加载所有permission数据;减少与数据的交互次数.
	@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
        AjaxRusult result = new AjaxRusult();

		try {
			//Demo 4 一次查询所有数据
			List<Permission> root = new ArrayList<Permission>();

			List<Permission> childredPermissons =  permissionService.queryAllPermission();
			for (Permission permission : childredPermissons) {
				//通过子查找父
				//子菜单
				Permission child = permission ; //假设为子菜单
				if(child.getPid() == null ){
					root.add(permission);
				}else{
					//父节点
					for (Permission innerpermission : childredPermissons) {
						if(child.getPid() == innerpermission.getId()){
							Permission parent = innerpermission;
							parent.getChildren().add(child);
							break ; //跳出内层循环,如果跳出外层循环,需要使用标签跳出
						}
					}
				}
			}

			result.setSuccess(true);
			result.setData(root);

		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		return result ;
	}


    /*//Demo3 - 采用递归调用来解决,许可树多个层次的问题.
	@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		AjaxRusult result = new AjaxRusult();
		try {
			List<Permission> root = new ArrayList<Permission>();
			//父
			Permission permission = permissionService.getRootPermission();
			root.add(permission);
			queryChildPermissions(permission);

			result.setSuccess(true);
			result.setData(root);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}
		return result ;
	}
   *//* *
     * 递归使用条件:
     * 	1.调用自身方法
     *  2.不断调用自身方法时,操作范围一定要缩小.
     *  3.一定要存在跳出条件.
     * @param permission
     *//*
    private void queryChildPermissions(Permission permission){
        List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());
        //组合父子关系
        permission.setChildren(children);
        for (Permission innerChildren : children) {
            queryChildPermissions(innerChildren);
        }
    }*/


    /*//Demo2-从数据表t_permission查询数据,显示许可树.
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadData(){
        AjaxRusult result = new AjaxRusult();
        try {
            ArrayList<Permission> root = new ArrayList<>();
            Permission rootPermission = permissionService.getRootPermission();
            root.add(rootPermission);
            List<Permission> childrenPermission = permissionService.getChildrenPermissionByPid(rootPermission.getId());
            rootPermission.setChildren(childrenPermission);
            for (Permission child : childrenPermission) {
                List<Permission> innerchilds = permissionService.getChildrenPermissionByPid(child.getId());
                child.setChildren(innerchilds);
            }
            result.setSuccess(true);
            result.setData(root);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败!");
        }
        return result ;
    }
*/



/*
    //Demo2-从数据表t_permission查询数据,显示许可树.
	@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		AjaxRusult result = new AjaxRusult();
		try {
			List<Permission> root = new ArrayList<Permission>();
			//父
			Permission permission = permissionService.getRootPermission();
			root.add(permission);
			//子
			queryPermission(permission);

			result.setSuccess(true);
			result.setData(root);
			}catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("加载许可树数据失败!");
		}

		return result ;
	}
			private void queryPermission(Permission permission){
			permission.setOpen(true);
			List<Permission> children = permissionService.getChildrenPermissionByPid(permission.getId());
			permission.setChildren(children);
			for (Permission child : children) {
				queryPermission(child);
		}
			}

*/

//Demo1Demo1 - 模拟数据生成树
    /*@ResponseBody
    @RequestMapping("/loadData")
    public Object loadData() {
        AjaxRusult result = new AjaxRusult();
        try {
         //父
            List<Permission> root = new ArrayList<Permission>();
            Permission permission = new Permission();
            permission.setName("系统权限菜单");
            permission.setOpen(true);

            root.add(permission);

            //子
            List<Permission> children = new ArrayList<Permission>();
            Permission permission1 = new Permission();
            permission1.setName("控制面板");

            Permission permission2 = new Permission();
            permission2.setName("权限管理");

            children.add(permission1);
            children.add(permission2);

            //设置父子关系
            permission.setChildren(children);

            result.setSuccess(true);
            result.setData(root);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败!");
        }

        return result;
    }*/

}