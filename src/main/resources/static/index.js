const app = Vue.createApp({
    data() {
        return {
            email: "",
            password: [],

            firstNameToReg: "",
            lastNameToReg: "",
            passwordToReg: [],
            repeatPassword: [],
        }
    },
    created() {

    },
    methods: {
        signIn() {
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    if (this.email.includes('@admin')) {
                        location.href = "/manager.html?"
                    } else {
                        location.href = "/accounts.html?"
                    }
                })
                .catch(err => {
                    swal({
                        title: "Alert",
                        text: "Email or password was not correct",
                        icon: "warning",
                    });
                })
        },
        register() {
            if (this.password === this.repeatPassword) {
                axios.post('/api/clients', "firstName=" + this.firstNameToReg + "&lastName=" + this.lastNameToReg + "&email=" + this.email + "&password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => {
                        swal({
                                title: "Congratulations",
                                text: "You can sign in whith your email and password",
                                icon: "success",
                                button: "OK",
                            })
                            .then(res => {
                                this.signIn();
                            })
                    })
            } else {
                swal({
                    title: "Alert",
                    text: "Password no repeat",
                    icon: "warning",
                })
            }

        }

    },
    computed: {

    },
})
app.mount("#app")