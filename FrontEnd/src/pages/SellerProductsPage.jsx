import { useEffect, useState } from "react";
import { apiRequest } from "../api/httpClient.js";
import { AdminLayout } from "../layouts/AdminLayout.jsx";
import { Package, Plus, Layers, Search, CheckCircle2, AlertCircle, ArrowLeft, RefreshCw, Box } from "lucide-react";
import { getImageForProduct } from "../utils/imageMapper.js";

export function SellerProductsPage() {
  const [activeTab, setActiveTab] = useState("list"); // 'list' | 'create_product' | 'create_variant'
  
  // Data states
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  // Product Form states
  const [productForm, setProductForm] = useState({
    productName: "",
    basePrice: "",
    description: "",
    weightGram: "500",
    categoryId: ""
  });
  const [productSubmitting, setProductSubmitting] = useState(false);
  const [productSuccess, setProductSuccess] = useState(null);
  const [productError, setProductError] = useState("");

  // Variant Form states
  const [variantForm, setVariantForm] = useState({
    productId: "",
    variantName: "",
    sku: "",
    priceModifier: "0",
    stock: "10"
  });
  const [variantSubmitting, setVariantSubmitting] = useState(false);
  const [variantSuccess, setVariantSuccess] = useState(null);
  const [variantError, setVariantError] = useState("");

  // Load data
  const fetchData = async () => {
    setLoading(true);
    setError("");
    try {
      const [prodsData, catsData] = await Promise.all([
        apiRequest("/api/v1/products/admin").catch(() => []),
        apiRequest("/api/v1/products/categories").catch(() => [])
      ]);
      setProducts(Array.isArray(prodsData) ? prodsData : (prodsData.content || []));
      setCategories(Array.isArray(catsData) ? catsData : []);
    } catch (e) {
      setError(e.message || "Không thể tải danh sách sản phẩm.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Quick action: Add variant for a specific product
  const handleOpenAddVariant = (productId) => {
    setVariantForm(prev => ({ ...prev, productId }));
    setVariantSuccess(null);
    setVariantError("");
    setActiveTab("create_variant");
  };

  // Submit Create Product
  const handleCreateProduct = async (e) => {
    e.preventDefault();
    setProductSubmitting(true);
    setProductError("");
    setProductSuccess(null);

    if (!productForm.productName.trim()) {
      setProductError("Tên sản phẩm không được trống.");
      setProductSubmitting(false);
      return;
    }
    if (!productForm.basePrice || Number(productForm.basePrice) < 0) {
      setProductError("Giá gốc phải lớn hơn hoặc bằng 0.");
      setProductSubmitting(false);
      return;
    }
    if (!productForm.categoryId) {
      setProductError("Vui lòng chọn hoặc nhập Danh mục sản phẩm.");
      setProductSubmitting(false);
      return;
    }

    try {
      const payload = {
        productName: productForm.productName.trim(),
        basePrice: Number(productForm.basePrice),
        description: productForm.description.trim(),
        weightGram: Number(productForm.weightGram || 0),
        categoryId: productForm.categoryId
      };

      const res = await apiRequest("/api/v1/products", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setProductSuccess(res);
      // Reset form
      setProductForm({
        productName: "",
        basePrice: "",
        description: "",
        weightGram: "500",
        categoryId: categories[0]?.id || ""
      });
      // Refresh list
      fetchData();
    } catch (err) {
      setProductError(err.message || "Lỗi khi tạo sản phẩm.");
    } finally {
      setProductSubmitting(false);
    }
  };

  // Submit Create Variant
  const handleCreateVariant = async (e) => {
    e.preventDefault();
    setVariantSubmitting(true);
    setVariantError("");
    setVariantSuccess(null);

    if (!variantForm.productId) {
      setVariantError("Vui lòng chọn Sản phẩm gốc.");
      setVariantSubmitting(false);
      return;
    }
    if (!variantForm.sku.trim()) {
      setVariantError("Mã SKU không được để trống.");
      setVariantSubmitting(false);
      return;
    }

    try {
      const payload = {
        productId: variantForm.productId,
        variantName: variantForm.variantName.trim(),
        sku: variantForm.sku.trim(),
        priceModifier: Number(variantForm.priceModifier || 0),
        stock: Number(variantForm.stock || 0)
      };

      const res = await apiRequest("/api/v1/products/variants", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setVariantSuccess(res);
      // Reset form SKU and variant name for convenient multiple variant additions
      setVariantForm(prev => ({
        ...prev,
        variantName: "",
        sku: "",
        priceModifier: "0",
        stock: "10"
      }));
      // Refresh list
      fetchData();
    } catch (err) {
      setVariantError(err.message || "Lỗi khi tạo biến thể.");
    } finally {
      setVariantSubmitting(false);
    }
  };

  const filteredProducts = products.filter(p => 
    p.productName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    p.categoryName?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <AdminLayout>
      {/* Header section */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <div>
          <h1 className="text-3xl font-black text-slate-900 mb-1">Quản Lý Sản Phẩm</h1>
          <p className="text-slate-500 font-medium">Tạo sản phẩm mới, thêm phân loại biến thể (Variant) và quản lý danh mục</p>
        </div>

        {/* Tab Switcher Buttons */}
        <div className="flex gap-2 bg-slate-200/70 p-1.5 rounded-xl self-start md:self-auto">
          <button
            onClick={() => setActiveTab("list")}
            className={`px-4 py-2 rounded-lg text-sm font-bold transition flex items-center gap-2 ${
              activeTab === "list" 
                ? "bg-white text-[#0d47a1] shadow-sm" 
                : "text-slate-600 hover:text-slate-900"
            }`}
          >
            <Package size={16} />
            Danh sách
          </button>
          <button
            onClick={() => {
              setActiveTab("create_product");
              setProductSuccess(null);
              setProductError("");
              if (categories.length > 0 && !productForm.categoryId) {
                setProductForm(prev => ({ ...prev, categoryId: categories[0].id }));
              }
            }}
            className={`px-4 py-2 rounded-lg text-sm font-bold transition flex items-center gap-2 ${
              activeTab === "create_product" 
                ? "bg-[#0d47a1] text-white shadow-sm" 
                : "text-slate-600 hover:text-slate-900"
            }`}
          >
            <Plus size={16} />
            Tạo Sản Phẩm
          </button>
          <button
            onClick={() => {
              setActiveTab("create_variant");
              setVariantSuccess(null);
              setVariantError("");
              if (products.length > 0 && !variantForm.productId) {
                setVariantForm(prev => ({ ...prev, productId: products[0].productId }));
              }
            }}
            className={`px-4 py-2 rounded-lg text-sm font-bold transition flex items-center gap-2 ${
              activeTab === "create_variant" 
                ? "bg-[#0d47a1] text-white shadow-sm" 
                : "text-slate-600 hover:text-slate-900"
            }`}
          >
            <Layers size={16} />
            Thêm Biến Thể
          </button>
        </div>
      </div>

      {/* TAB 1: PRODUCT LIST */}
      {activeTab === "list" && (
        <div>
          <div className="flex justify-between items-center mb-6">
            <div className="relative w-full md:w-80">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
                <Search size={16} />
              </div>
              <input
                type="text"
                placeholder="Tìm kiếm sản phẩm..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 pr-4 py-2.5 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1] w-full bg-white shadow-sm"
              />
            </div>
            
            <button
              onClick={fetchData}
              className="p-2.5 bg-white border border-slate-200 rounded-xl text-slate-600 hover:text-[#0d47a1] hover:border-blue-200 transition shadow-sm"
              title="Làm mới danh sách"
            >
              <RefreshCw size={18} className={loading ? "animate-spin" : ""} />
            </button>
          </div>

          {loading && (
            <div className="py-20 flex flex-col items-center justify-center text-[#0d47a1]">
              <Box className="animate-bounce mb-4" size={40} />
              <p className="font-bold text-slate-600">Đang tải danh sách sản phẩm...</p>
            </div>
          )}

          {error && (
            <div className="rounded-2xl border border-red-200 bg-red-50 p-6 text-center text-red-700 shadow-sm mb-6">
              <p className="font-bold mb-1">Không thể tải dữ liệu</p>
              <p className="text-sm">{error}</p>
            </div>
          )}

          {!loading && !error && (
            <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                  <thead>
                    <tr className="bg-slate-50 border-b border-slate-200 text-xs font-bold text-slate-500 uppercase tracking-wider">
                      <th className="p-4 pl-6">Sản phẩm</th>
                      <th className="p-4">Danh mục</th>
                      <th className="p-4">Giá gốc</th>
                      <th className="p-4">Biến thể & Tồn kho</th>
                      <th className="p-4 text-right pr-6">Hành động</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {filteredProducts.length === 0 ? (
                      <tr>
                        <td colSpan={5} className="p-12 text-center text-slate-500">
                          <div className="flex flex-col items-center justify-center">
                            <Package size={48} className="mb-4 opacity-30 text-slate-400" strokeWidth={1} />
                            <p className="font-bold text-slate-700 text-lg mb-1">Chưa có sản phẩm nào</p>
                            <p className="text-sm text-slate-500 mb-4">Hãy tạo sản phẩm đầu tiên của bạn để bắt đầu bán hàng</p>
                            <button
                              onClick={() => setActiveTab("create_product")}
                              className="px-4 py-2 bg-[#0d47a1] text-white rounded-xl font-bold text-sm shadow-md hover:bg-blue-800 transition flex items-center gap-2"
                            >
                              <Plus size={16} /> Tạo sản phẩm mới
                            </button>
                          </div>
                        </td>
                      </tr>
                    ) : (
                      filteredProducts.map((product) => {
                        const totalStock = product.variants?.reduce((sum, v) => sum + (v.quantityInStock || 0), 0) || 0;
                        return (
                          <tr key={product.productId} className="hover:bg-slate-50 transition">
                            <td className="p-4 pl-6">
                              <div className="flex items-center gap-4">
                                <div className="w-12 h-12 rounded-xl bg-slate-100 border border-slate-200 flex items-center justify-center shrink-0 overflow-hidden relative shadow-sm">
                                  <img src={getImageForProduct(product.productName)} alt={product.productName} className="w-full h-full object-cover" />
                                </div>
                                <div>
                                  <p className="font-bold text-slate-900">{product.productName}</p>
                                  <p className="text-xs text-slate-500 line-clamp-1">{product.description || "Chưa có mô tả"}</p>
                                  <span className="text-[10px] text-slate-400 font-mono">ID: {product.productId}</span>
                                </div>
                              </div>
                            </td>
                            <td className="p-4">
                              <span className="inline-block px-3 py-1 bg-blue-50 text-[#0d47a1] rounded-lg text-xs font-bold border border-blue-100">
                                {product.categoryName || "Mặc định"}
                              </span>
                            </td>
                            <td className="p-4">
                              <p className="font-black text-[#0d47a1]">
                                {Number(product.basePrice).toLocaleString("vi-VN")}₫
                              </p>
                            </td>
                            <td className="p-4">
                              <div className="space-y-1">
                                <div className="text-xs font-semibold text-slate-700">
                                  Tổng tồn: <span className="font-black text-[#0d47a1]">{totalStock}</span> | {product.variants?.length || 0} biến thể
                                </div>
                                <div className="flex flex-wrap gap-1.5 max-w-xs">
                                  {product.variants?.map(v => (
                                    <span key={v.id} className="px-2 py-0.5 bg-slate-100 border border-slate-200 rounded text-[11px] text-slate-700 font-medium">
                                      {v.sku}: <b>{v.quantityInStock}</b>
                                    </span>
                                  ))}
                                </div>
                              </div>
                            </td>
                            <td className="p-4 text-right pr-6">
                              <button
                                onClick={() => handleOpenAddVariant(product.productId)}
                                className="px-3 py-1.5 bg-slate-100 hover:bg-[#0d47a1] hover:text-white text-slate-700 rounded-lg text-xs font-bold border border-slate-200 transition inline-flex items-center gap-1.5 shadow-sm"
                              >
                                <Plus size={14} /> Thêm biến thể
                              </button>
                            </td>
                          </tr>
                        );
                      })
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      )}

      {/* TAB 2: CREATE PRODUCT FORM */}
      {activeTab === "create_product" && (
        <div className="max-w-3xl mx-auto">
          <div className="bg-white border border-slate-200 rounded-3xl p-8 shadow-sm">
            <div className="flex items-center gap-3 mb-6 pb-4 border-b border-slate-100">
              <button
                onClick={() => setActiveTab("list")}
                className="p-2 text-slate-400 hover:text-slate-700 hover:bg-slate-100 rounded-xl transition"
              >
                <ArrowLeft size={20} />
              </button>
              <div>
                <h2 className="text-xl font-black text-slate-900">Tạo Sản Phẩm Mới</h2>
                <p className="text-xs text-slate-500 font-medium">API: POST /api/v1/products</p>
              </div>
            </div>

            {productSuccess && (
              <div className="mb-6 p-5 bg-emerald-50 border border-emerald-200 rounded-2xl flex items-start gap-4">
                <CheckCircle2 size={24} className="text-emerald-600 shrink-0 mt-0.5" />
                <div className="flex-1">
                  <h4 className="font-bold text-emerald-900 mb-1">Tạo sản phẩm thành công!</h4>
                  <p className="text-xs text-emerald-700 font-mono mb-3">
                    ID Sản phẩm: <span className="font-bold bg-emerald-100 px-2 py-0.5 rounded">{productSuccess.productId}</span>
                  </p>
                  <div className="flex gap-3">
                    <button
                      onClick={() => handleOpenAddVariant(productSuccess.productId)}
                      className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-xs rounded-xl shadow transition flex items-center gap-1.5"
                    >
                      <Layers size={14} /> Tạo biến thể (Variant) cho sản phẩm này ngay
                    </button>
                    <button
                      onClick={() => setProductSuccess(null)}
                      className="px-3 py-2 bg-white text-emerald-800 border border-emerald-300 font-bold text-xs rounded-xl hover:bg-emerald-50 transition"
                    >
                      Tạo tiếp sản phẩm khác
                    </button>
                  </div>
                </div>
              </div>
            )}

            {productError && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-2xl flex items-center gap-3 text-red-700 text-sm font-medium">
                <AlertCircle size={20} className="shrink-0 text-red-500" />
                <span>{productError}</span>
              </div>
            )}

            <form onSubmit={handleCreateProduct} className="space-y-6">
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                  Tên sản phẩm <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  placeholder="Ví dụ: Áo sơ mi nam cao cấp, Điện thoại iPhone 15 Pro..."
                  value={productForm.productName}
                  onChange={(e) => setProductForm({ ...productForm, productName: e.target.value })}
                  className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                  required
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Giá gốc (VNĐ) <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="number"
                    min="0"
                    step="1000"
                    placeholder="150000"
                    value={productForm.basePrice}
                    onChange={(e) => setProductForm({ ...productForm, basePrice: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Danh mục <span className="text-red-500">*</span>
                  </label>
                  {categories.length > 0 ? (
                    <select
                      value={productForm.categoryId}
                      onChange={(e) => setProductForm({ ...productForm, categoryId: e.target.value })}
                      className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1] bg-white"
                      required
                    >
                      <option value="">-- Chọn danh mục --</option>
                      {categories.map((cat) => (
                        <option key={cat.id} value={cat.id}>
                          {cat.name} ({cat.id})
                        </option>
                      ))}
                    </select>
                  ) : (
                    <input
                      type="text"
                      placeholder="Nhập Category UUID..."
                      value={productForm.categoryId}
                      onChange={(e) => setProductForm({ ...productForm, categoryId: e.target.value })}
                      className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                      required
                    />
                  )}
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                  Trọng lượng (Gram)
                </label>
                <input
                  type="number"
                  min="0"
                  placeholder="500"
                  value={productForm.weightGram}
                  onChange={(e) => setProductForm({ ...productForm, weightGram: e.target.value })}
                  className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                />
              </div>

              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                  Mô tả sản phẩm
                </label>
                <textarea
                  rows={4}
                  placeholder="Mô tả chi tiết về sản phẩm, đặc điểm nổi bật..."
                  value={productForm.description}
                  onChange={(e) => setProductForm({ ...productForm, description: e.target.value })}
                  className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                />
              </div>

              <div className="pt-4 flex justify-end gap-3">
                <button
                  type="button"
                  onClick={() => setActiveTab("list")}
                  className="px-6 py-3 border border-slate-200 text-slate-600 rounded-xl text-sm font-bold hover:bg-slate-50 transition"
                >
                  Hủy bỏ
                </button>
                <button
                  type="submit"
                  disabled={productSubmitting}
                  className="px-8 py-3 bg-[#0d47a1] hover:bg-blue-800 text-white rounded-xl text-sm font-bold shadow-lg shadow-blue-900/20 transition disabled:opacity-50 flex items-center gap-2"
                >
                  {productSubmitting ? "Đang xử lý..." : "Lưu Sản Phẩm"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* TAB 3: CREATE VARIANT FORM */}
      {activeTab === "create_variant" && (
        <div className="max-w-3xl mx-auto">
          <div className="bg-white border border-slate-200 rounded-3xl p-8 shadow-sm">
            <div className="flex items-center gap-3 mb-6 pb-4 border-b border-slate-100">
              <button
                onClick={() => setActiveTab("list")}
                className="p-2 text-slate-400 hover:text-slate-700 hover:bg-slate-100 rounded-xl transition"
              >
                <ArrowLeft size={20} />
              </button>
              <div>
                <h2 className="text-xl font-black text-slate-900">Tạo Biến Thể Sản Phẩm (Variant)</h2>
                <p className="text-xs text-slate-500 font-medium">API: POST /api/v1/products/variants</p>
              </div>
            </div>

            {variantSuccess && (
              <div className="mb-6 p-5 bg-emerald-50 border border-emerald-200 rounded-2xl flex items-start gap-4">
                <CheckCircle2 size={24} className="text-emerald-600 shrink-0 mt-0.5" />
                <div className="flex-1">
                  <h4 className="font-bold text-emerald-900 mb-1">Tạo biến thể thành công!</h4>
                  <p className="text-xs text-emerald-700 font-mono mb-3">
                    ID Biến thể: <span className="font-bold bg-emerald-100 px-2 py-0.5 rounded">{variantSuccess.variantId}</span>
                  </p>
                  <div className="flex gap-3">
                    <button
                      onClick={() => setVariantSuccess(null)}
                      className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-xs rounded-xl shadow transition flex items-center gap-1.5"
                    >
                      <Plus size={14} /> Thêm tiếp biến thể khác cho sản phẩm này
                    </button>
                    <button
                      onClick={() => setActiveTab("list")}
                      className="px-3 py-2 bg-white text-emerald-800 border border-emerald-300 font-bold text-xs rounded-xl hover:bg-emerald-50 transition"
                    >
                      Xem danh sản phẩm & tồn kho
                    </button>
                  </div>
                </div>
              </div>
            )}

            {variantError && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-2xl flex items-center gap-3 text-red-700 text-sm font-medium">
                <AlertCircle size={20} className="shrink-0 text-red-500" />
                <span>{variantError}</span>
              </div>
            )}

            <form onSubmit={handleCreateVariant} className="space-y-6">
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                  Sản phẩm gốc <span className="text-red-500">*</span>
                </label>
                {products.length > 0 ? (
                  <select
                    value={variantForm.productId}
                    onChange={(e) => setVariantForm({ ...variantForm, productId: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1] bg-white"
                    required
                  >
                    <option value="">-- Chọn sản phẩm --</option>
                    {products.map((p) => (
                      <option key={p.productId} value={p.productId}>
                        {p.productName} ({p.productId})
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    type="text"
                    placeholder="Nhập Product UUID..."
                    value={variantForm.productId}
                    onChange={(e) => setVariantForm({ ...variantForm, productId: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                    required
                  />
                )}
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Tên biến thể
                  </label>
                  <input
                    type="text"
                    placeholder="Ví dụ: Màu đỏ / XL, 128GB / Đen..."
                    value={variantForm.variantName}
                    onChange={(e) => setVariantForm({ ...variantForm, variantName: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Mã SKU duy nhất <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="text"
                    placeholder="Ví dụ: SKU-SHIRT-RED-XL"
                    value={variantForm.sku}
                    onChange={(e) => setVariantForm({ ...variantForm, sku: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1] font-mono"
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Chênh lệch giá (Price Modifier)
                  </label>
                  <input
                    type="number"
                    step="1000"
                    placeholder="0 (Nhập số dương để cộng thêm giá, âm để giảm)"
                    value={variantForm.priceModifier}
                    onChange={(e) => setVariantForm({ ...variantForm, priceModifier: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                  />
                  <p className="text-[11px] text-slate-400 mt-1">Ví dụ: Giá gốc 100k, nhập 20000 -> Giá biến thể = 120k</p>
                </div>

                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider text-slate-700 mb-2">
                    Số lượng tồn kho ban đầu
                  </label>
                  <input
                    type="number"
                    min="0"
                    placeholder="10"
                    value={variantForm.stock}
                    onChange={(e) => setVariantForm({ ...variantForm, stock: e.target.value })}
                    className="w-full px-4 py-3 border border-slate-200 rounded-xl text-sm font-medium focus:outline-none focus:border-[#0d47a1] focus:ring-1 focus:ring-[#0d47a1]"
                  />
                </div>
              </div>

              <div className="pt-4 flex justify-end gap-3">
                <button
                  type="button"
                  onClick={() => setActiveTab("list")}
                  className="px-6 py-3 border border-slate-200 text-slate-600 rounded-xl text-sm font-bold hover:bg-slate-50 transition"
                >
                  Hủy bỏ
                </button>
                <button
                  type="submit"
                  disabled={variantSubmitting}
                  className="px-8 py-3 bg-[#0d47a1] hover:bg-blue-800 text-white rounded-xl text-sm font-bold shadow-lg shadow-blue-900/20 transition disabled:opacity-50 flex items-center gap-2"
                >
                  {variantSubmitting ? "Đang xử lý..." : "Lưu Biến Thể"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminLayout>
  );
}
